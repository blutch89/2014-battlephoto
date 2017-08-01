package ch.blutch.battlephoto.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.controller.utils.UserUtil;
import ch.blutch.battlephoto.controller.utils.ViewParameters;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.service.impl.PhotoServiceImpl;
import ch.blutch.battlephoto.model.service.impl.UserServiceImpl;

@ManagedBean
@SessionScoped
@Controller
@Scope("session")
public class MyPhotosController {

	@Autowired
	private PhotoServiceImpl photoService;
	
	@Autowired
	private UserServiceImpl userService;
	
	// Paramètres
	
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	public List<Photo> findMyPhotos() {
		List<Photo> myPhotos = new ArrayList<>();
		
		if (UserUtil.isLogged()) {
			int userId = userService.getUserFromUsername(UserUtil.getUsername()).getId();
			
			myPhotos = userService.findPhotosUser(userId);
		}
		
		
		return myPhotos;
	}
	
	public List<Category> findCategories(int photoId) {
		// Test avec "> 0" suite à un bug bizarre...
		return photoId > 0 ? photoService.findCategories(photoId) : null;
	}
	
	public void deletePhoto(Photo photo) {
		// Suppression des fichiers images
		File smallImageFile = new File(ViewParameters.photoDestinationPath + "/" + photo.getFileName() + "-small.jpg");
		File largeImageFile = new File(ViewParameters.photoDestinationPath + "/" + photo.getFileName() + "-large.jpg");
		
		smallImageFile.delete();
		largeImageFile.delete();
		
		// Suppression de la photo dans la bdd
		photoService.delete(photo);
		
		// Recalcul le score du user
		userService.updateUserScore(photo.getOwner().getId());
	}
	
}
