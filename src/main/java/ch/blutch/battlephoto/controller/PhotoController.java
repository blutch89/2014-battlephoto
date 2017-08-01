package ch.blutch.battlephoto.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import ch.blutch.battlephoto.controller.utils.DateUtil;
import ch.blutch.battlephoto.controller.utils.UserUtil;
import ch.blutch.battlephoto.controller.utils.ViewParameters;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Note;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.entity.User;
import ch.blutch.battlephoto.model.impl.UserGrade;
import ch.blutch.battlephoto.model.service.GenericService;
import ch.blutch.battlephoto.model.service.impl.CategoryServiceImpl;
import ch.blutch.battlephoto.model.service.impl.GenericServiceImpl;
import ch.blutch.battlephoto.model.service.impl.PhotoServiceImpl;
import ch.blutch.battlephoto.model.service.impl.UserServiceImpl;
import ch.blutch.battlephoto.model.utils.ModelParameters;

@ManagedBean
@RequestScoped
@Controller
@Scope("request")
public class PhotoController {

	@Autowired
	private CategoryServiceImpl categoryService;
	
	@Autowired
	private PhotoServiceImpl photoService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private GenericServiceImpl<Note> noteService;
	
	// Paramètres
	private Integer photoId;
	private Photo photo;
	private boolean hasPhoto;
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		// PhotoId + photo
		if (requestParameters.get("photoId") != null && !requestParameters.get("photoId").equals("0")) {
			photoId = Integer.parseInt(requestParameters.get("photoId"));
			photo = photoService.findById(photoId);
			hasPhoto = true;
		} else {
			photoId = null;
			hasPhoto = false;
		}
	}
	
	public String createPageTitle() {
		String description = photo.getDescription();
		String shortDescription = description;
		
		if (description.length() > 30) {
			shortDescription = description.substring(0, 30) + "...";
		}
		
		return "BattlePhoto - " + shortDescription;
	}
	
	public List<Category> findCategories() {
		// Test avec "> 0" suite à un bug bizarre...
		return photoId > 0 ? photoService.findCategories(photoId) : null;
	}
	
	private String findIpAddress() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		
		if (ipAddress == null) {
		    ipAddress = request.getRemoteAddr();
		}
		
		return ipAddress;
	}
	
	public String canNote() {
		String ipAddress = findIpAddress();
		
		// Si le user est logué et qu'il a déjà voté, ne peut pas voter.
		if (UserUtil.isLogged()) {
			int userId = userService.getUserFromUsername(UserUtil.getUsername()).getId();
			
			if (!photoService.userCanNote(userId, photoId)) {
				return "true";
			}
			
			return "false";
		} else {	// Si le user n'est pas logué, testsi son adresse ip a déjà voté. Si oui, il ne pourra pas voter.
			if (!photoService.ipAddressCanNote(ipAddress, photoId)) {
				return "true";
			}
			
			return "false";
		}
	}
	
	public void addNoteToPhoto() {
		User user = null;
		String ipAddress = findIpAddress();
		
		// Récupère le user
		if (UserUtil.isLogged()) {
			user = userService.getUserFromUsername(UserUtil.getUsername());
		}
		
		// Sauvegarde la note
		Note note = new Note();
		note.setDateOfNote(new Date());
		note.setIpAddress(ipAddress);
		note.setNote(1);
		note.setUser(user);
		note.setPhoto(photoService.findById(photoId));
		
		noteService.merge(note);
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public boolean isHasPhoto() {
		return hasPhoto;
	}

	public void setHasPhoto(boolean hasPhoto) {
		this.hasPhoto = hasPhoto;
	}
}
