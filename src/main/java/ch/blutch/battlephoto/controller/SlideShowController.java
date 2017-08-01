package ch.blutch.battlephoto.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.controller.utils.UserUtil;
import ch.blutch.battlephoto.controller.utils.ViewParameters;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Note;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.entity.User;
import ch.blutch.battlephoto.model.impl.UserGrade;
import ch.blutch.battlephoto.model.service.impl.CategoryServiceImpl;
import ch.blutch.battlephoto.model.service.impl.GenericServiceImpl;
import ch.blutch.battlephoto.model.service.impl.PhotoServiceImpl;
import ch.blutch.battlephoto.model.service.impl.UserServiceImpl;

@ManagedBean
@ViewScoped
@Controller
@Scope("view")
public class SlideShowController {

	@Autowired
	private CategoryServiceImpl categoryService;
	
	@Autowired
	private PhotoServiceImpl photoService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private GenericServiceImpl<Note> noteService;
	
	// Paramètres
	private Category category;
	private boolean isBestPhotos = false;
	private Integer photoId;
	private List<Photo> photosList = new ArrayList<>();
	private boolean photosFull = false;
	
	@PostConstruct
	public void postConstruct() {
		noteService.setClazz(Note.class);
		
		loadParameters();
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		// Param category
		Integer categoryId;
		
		if (requestParameters.get("idCat") != null && !requestParameters.get("idCat").equals("0")) {
			categoryId = Integer.parseInt(requestParameters.get("idCat"));
		} else {
			categoryId = null;
		}
		
		category = categoryId != null ? categoryService.findCategoryById(categoryId) : null;
	
		// Param isBestPhotos
		if (requestParameters.get("isBest") != null) {
			isBestPhotos = Boolean.parseBoolean(requestParameters.get("isBest"));
		} else {
			isBestPhotos = false;
		}
		
		// Param photoId
		if (requestParameters.get("photoId") != null) {
			photoId = Integer.parseInt(requestParameters.get("photoId"));
		} else {
			photoId = null;
		}
	}
	
	public String createPageTitle() {
		if (isBestPhotos == false) {
			String title = category != null ? category.getDescriptionFr() : ViewParameters.noCategoryMessage;
			return "Diaporama - " + title;
		} else {
			String title = category != null ? category.getDescriptionFr() : ViewParameters.noCategoryMessage;
			return "Meilleures photos - " + title;
		}
	}
	
	public String isBestPhotos() {
		return isBestPhotos == true ? "true" : "false";
	}
	
	public String getCategoryName() {
		String toReturn = category != null ? category.getDescriptionFr() : ViewParameters.noCategoryMessage;
		
		return toReturn;
	}
	
	public boolean isCategoryHasNoPhotos() {
		if (category != null) {
			int nbPhotos = findNbPhotosInCategoryAndSubCategories(category.getId());
			
			return nbPhotos == 0 ? true : false;
		} else {
			return false;
		}
	}
	
	public List<Category> findSubCategories() {
		// Si aucune catégorie n'a été spécifiée
		if (category == null) {
			return categoryService.findParentsCategories();
		} else {
			return categoryService.findSubCategories(category.getId());
		}
	}
	
	public int findNbPhotosInCategoryAndSubCategories(int id) {
		int nbPhotos = categoryService.findNbPhotosInCategoryAndSubCategories(id);
		
		return nbPhotos;
	}
	
	public List<Photo> findXPhotos() {
		List<Photo> photos = new ArrayList<>();
		int nbTotalPhotos = 0;
		
		// Récupère les photos en random ou best
		if (isBestPhotos == false) {
			photos = findXRandomPhotosFromCategory();
		} else {
			photos = findXBestPhotosFromCategory();
		}
		
		// Calcul le nombre total de photos
		if (category != null) {
			nbTotalPhotos = categoryService.findNbPhotosInCategoryAndSubCategories(category.getId());
		} else {
			nbTotalPhotos = photoService.findNbTotalPhotos();
		}
		
		// Gère le stop du waypoint d'infinite scroll
		if (photosList.size() + photos.size() >= nbTotalPhotos) {
			photosFull = true;
		}
		
		return photos;
	}
	
	public List<Photo> findXRandomPhotosFromCategory() {
		if (category != null) {
			return categoryService.findXRandomPhotosFromCategory(category.getId(), photosList);
		} else {
			return photoService.findXRandomPhotos(photosList);
		}
	}
	
	public List<Photo> findXBestPhotosFromCategory() {
		if (category != null) {
			return categoryService.findXBestPhotosFromCategory(category.getId(), photosList.size());
		} else {
			return photoService.findXBestPhotos(photosList.size(), false);
		}
	}
	
	public void loadMorePhotos() {System.out.println("!!!!" + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024));
		photosList.addAll(findXPhotos());
	}
	
	// Utile pour savoir si oui ou non on affiche la zone de sous catégories
	public boolean hasSubCategories() {
		if (category == null) {
			return true;
		}
		
		return categoryService.hasSubCategories(category.getId());
	}
	
	private String findIpAddress() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		
		if (ipAddress == null) {
		    ipAddress = request.getRemoteAddr();
		}
		
		return ipAddress;
	}
	
	public String canNote(int photoId_) {
		String ipAddress = findIpAddress();
		int photoIdUse;
		
		// Le paramètre de méthode "photoId_" a toujours une valeur.
		// Si le paramètre de request "photoId" a une valeur, utilise cette valeur.
		// Ce paramètre de request a une valeur uniquement quand on clique sur le bouton "+1" en ajax.
		if (this.photoId != null) {
			photoIdUse = this.photoId;
		} else {
			photoIdUse = photoId_;
		}
		
		this.photoId = null;
		
		// Si le user est logué et qu'il a déjà voté, ne peut pas voter.
		if (UserUtil.isLogged()) {
			int userId = userService.getUserFromUsername(UserUtil.getUsername()).getId();
			
			if (!photoService.userCanNote(userId, photoIdUse)) {
				return "true";
			}
			
			return "false";
		} else {	// Si le user n'est pas logué, testsi son adresse ip a déjà voté. Si oui, il ne pourra pas voter.
			if (!photoService.ipAddressCanNote(ipAddress, photoIdUse)) {
				return "true";
			}
			
			return "false";
		}
		
		
	}
	
	private void displayParam() {
		System.out.println("!!ok1");
		for (Entry<String, String> entry : FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().entrySet()) {
			System.out.println(entry.getKey() + "/" + entry.getValue());
		}
		System.out.println("!!ok2");
	}
	
	public void addNoteToPhoto() {
		loadParameters();
		User user = null;
		Photo photo = photoService.findById(photoId);
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
		note.setPhoto(photo);
		
		noteService.merge(note);
		
		// Recalcul le score de la photo et du user
		photoService.updatePhotoScore(photoId);
		userService.updateUserScore(photo.getOwner().getId());
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public List<Photo> getPhotosList() {
		if (photosList.size() == 0) {
			photosList = findXPhotos();
		}
		
		return photosList;
	}

	public void setPhotosList(List<Photo> photosList) {
		this.photosList = photosList;
	}

	public boolean isPhotosFull() {
		return photosFull;
	}

	public void setPhotosFull(boolean photosFull) {
		this.photosFull = photosFull;
	}
}
