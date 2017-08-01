package ch.blutch.battlephoto.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.controller.utils.UserUtil;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.entity.User;
import ch.blutch.battlephoto.model.impl.UserGrade;
import ch.blutch.battlephoto.model.service.impl.CategoryServiceImpl;
import ch.blutch.battlephoto.model.service.impl.PhotoServiceImpl;
import ch.blutch.battlephoto.model.service.impl.UserServiceImpl;

@ManagedBean
@RequestScoped
@Controller
@Scope("request")
public class UserController {
	
	@Autowired
	private CategoryServiceImpl categoryService;
	
	@Autowired
	private PhotoServiceImpl photoService;
	
	@Autowired
	private UserServiceImpl userService;
	
	// Paramètres
	private Integer userId;
	private User user;
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		// userId
		if (requestParameters.get("userId") != null && !requestParameters.get("userId").equals("0")) {
			userId = Integer.parseInt(requestParameters.get("userId"));
			user = userService.findById(userId);
		} else {
			userId = null;
			user = null;
		}
	}
	
	public String createPageTitle() {
		return user != null ? user.getUsername() : "Page utilisateur";
	}
	
	public int getNbPhotos() {
		int nbPhotos = userService.getNbPhotosUser(userId);
		
		return nbPhotos;
	}
	
	public List<Photo> findMyPhotos() {
		List<Photo> myPhotos = userService.findPhotosUser(userId);
		
		return myPhotos;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
