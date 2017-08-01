package ch.blutch.battlephoto.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.component.picklist.PickList;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import ch.blutch.battlephoto.controller.utils.DateUtil;
import ch.blutch.battlephoto.controller.utils.FileUtil;
import ch.blutch.battlephoto.controller.utils.ImageUtil;
import ch.blutch.battlephoto.controller.utils.StringUtil;
import ch.blutch.battlephoto.controller.utils.UserUtil;
import ch.blutch.battlephoto.controller.utils.ViewParameters;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.service.impl.CategoryServiceImpl;
import ch.blutch.battlephoto.model.service.impl.PhotoServiceImpl;
import ch.blutch.battlephoto.model.service.impl.UserServiceImpl;

@ManagedBean
@RequestScoped
@Controller
@Scope("request")
public class ImportPhotosEditController {

	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private CategoryServiceImpl categoryService;
	
	@Autowired
	private PhotoServiceImpl photoService;
	
	// Import photos edit
	private File currentPhoto;
	private String currentPhotoPath;
	private DualListModel<Category> dualSelectedCategories;
	private String description;
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
		
		// Chargement d'une photo
		File userUploadFile = new File(ViewParameters.uploadDestinationPath + "/" + UserUtil.getUsername());
		
		if (userUploadFile.exists()) {
			File[] photosUploaded = userUploadFile.listFiles();
			
			// S'il reste au moins une photo à modifer. Sinon, redirige l'utilisateur
			if (photosUploaded.length > 0) {
				currentPhoto = photosUploaded[0];
				currentPhotoPath = UserUtil.getUsername() + "/" + currentPhoto.getName();
			} else {
				userUploadFile.delete();
				redirectUploadEndMessage();
			}
		} else {
			redirectUploadEndMessage();
		}
		
		// Création des données du pickList
		List<Category> categories = categoryService.findSelectablesCategories();
		List<Category> categoriesTarget = new ArrayList<>();
		dualSelectedCategories = new DualListModel<Category>(categories, categoriesTarget);
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	public void deleteAndNext() {
		currentPhoto.delete();
		
		// Redirige l'utilisateur
		redirectNextPhoto();
	}
	
	public void saveAndNext() {
		int userId = userService.getUserFromUsername(UserUtil.getUsername()).getId();
		List<Category> categories = dualSelectedCategories.getTarget();
		Date captureDate = new Date();
		
		// Cherche un filename
		String photoExtension = FilenameUtils.getExtension(currentPhoto.getName());
		File tempFile = null;
		String tempFilename = null;
		
		do {
			tempFilename = StringUtil.generateRandomString(ViewParameters.photoNbCharInFileName);
			tempFile = new File(ViewParameters.photoDestinationPath + "/" + tempFilename + "." + ViewParameters.photoExtension);
		} while (tempFile.exists());
		
		
		// Resize et compresse
		String uploadPath = ViewParameters.uploadDestinationPath + "/" + UserUtil.getUsername();
		String sourceLargePath = uploadPath + "/" + tempFilename + "-large." + ViewParameters.photoExtension;
		String sourceSmallPath = uploadPath + "/" + tempFilename + "-small." + ViewParameters.photoExtension;
		File destinationLargeFile = new File(ViewParameters.photoDestinationPath + "/" + tempFilename + "-large." + ViewParameters.photoExtension);
		File destinationSmallFile = new File(ViewParameters.photoDestinationPath + "/" + tempFilename + "-small." + ViewParameters.photoExtension);
		
		resizeAndCompress(currentPhoto.getAbsolutePath(), sourceLargePath, 1);	// Grande photo
		resizeAndCompress(currentPhoto.getAbsolutePath(), sourceSmallPath, 2);	// Petite photo
		
		// Récupère la date du cliché
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(currentPhoto);
			
			ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
			
			if (directory == null) {
				captureDate = new Date();
			} else {
				captureDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
			}
		} catch (ImageProcessingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Déplace la photo dans le dossier
		FileUtil.copyFile(new File(sourceLargePath), destinationLargeFile);
		FileUtil.copyFile(new File(sourceSmallPath), destinationSmallFile);
		
		currentPhoto.delete();
		new File(sourceLargePath).delete();
		new File(sourceSmallPath).delete();
		
		
		// Enregistre la photo dans la bdd
		Photo photo = new Photo();
		photo.setCreationDate(new Date());
		photo.setCaptureDate(captureDate);
		photo.setDescription(description);
		photo.setOwner(userService.findById(userId));
		photo.setCategories(new HashSet<>(categories));
		photo.setFileName(tempFilename);
		
		photoService.merge(photo);
		
		// Recalcul le score du user
		userService.updateUserScore(userId);
		
		// Redirige l'utilisateur
		redirectNextPhoto();
	}
	
	private void redirectNextPhoto() {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void redirectUploadEndMessage() {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect("/" + ec.getContextName().toLowerCase() + "/message/import-photos-edit-success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Format : 1 -> grand, 2 -> miniature
	private void resizeAndCompress(String sourcePath, String destinationPath, int format) {
		int maxSize = format == 1 ? ViewParameters.photoLargeFormat : ViewParameters.photoSmallFormat;
		String photoExtension = FilenameUtils.getExtension(sourcePath);
		
		// Détermine le type de l'image
		int type = ImageUtil.IMAGE_UNKNOWN;
		
		if (photoExtension.toLowerCase().equals("jpg") || photoExtension.toLowerCase().equals("jpeg")) {
			type = ImageUtil.IMAGE_JPEG;
		} else if (photoExtension.toLowerCase().equals("png")) {
			type = ImageUtil.IMAGE_PNG;
		} else if (photoExtension.toLowerCase().equals("gif")) {
			type = ImageUtil.IMAGE_GIF;
		}
		
		// Resize l'image
		BufferedImage bi = ImageUtil.resizeImage(sourcePath, type, maxSize, maxSize);
		
		// Compress l'image
		if (type == ImageUtil.IMAGE_PNG) {
			ImageUtil.saveImage(bi, destinationPath, type);
		} else {
			ImageUtil.saveCompressedImage(bi, destinationPath, type);
		}
	}

	public DualListModel<Category> getDualSelectedCategories() {
		return dualSelectedCategories;
	}

	public void setDualSelectedCategories(
			DualListModel<Category> dualSelectedCategories) {
		this.dualSelectedCategories = dualSelectedCategories;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getCurrentPhoto() {
		return currentPhoto;
	}

	public void setCurrentPhoto(File currentPhoto) {
		this.currentPhoto = currentPhoto;
	}

	public String getCurrentPhotoPath() {
		return currentPhotoPath;
	}

	public void setCurrentPhotoPath(String currentPhotoPath) {
		this.currentPhotoPath = currentPhotoPath;
	}
}
