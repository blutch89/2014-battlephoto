package ch.blutch.battlephoto.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.service.impl.CategoryServiceImpl;
import ch.blutch.battlephoto.model.service.impl.PhotoServiceImpl;
import ch.blutch.battlephoto.model.service.impl.UserServiceImpl;

@ManagedBean
@RequestScoped
@Controller
@Scope("request")
public class MyPhotosEditController {

	@Autowired
	private PhotoServiceImpl photoService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private CategoryServiceImpl categoryService;
	
	// Paramètres
	private Integer photoId;
	
	// Paramètres vue
	private Photo photo;
	private DualListModel<Category> dualSelectedCategories;
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
		
		// Init paramètres
		photo = photoService.findById(photoId);
		
		List<Category> categories = categoryService.findSelectablesCategories();
		List<Category> categoriesTarget = new ArrayList<>(photoService.findCategories(photo.getId()));
		dualSelectedCategories = new DualListModel<Category>(categories, categoriesTarget);
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		if (requestParameters.get("idPhoto") != null && !requestParameters.get("idPhoto").equals("0")) {
			photoId = Integer.parseInt(requestParameters.get("idPhoto"));
		} else {
			photoId = null;
		}
	}
	
	public void save() {
		// Modification de la photo
		photo.setCategories(new HashSet<>(dualSelectedCategories.getTarget()));
		
		photoService.saveOrUpdate(photo);
		
		// Redirige l'utilisateur sur la page myphotos
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect("/" + ec.getContextName().toLowerCase() + "/user/myphotos");
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public DualListModel<Category> getDualSelectedCategories() {
		return dualSelectedCategories;
	}

	public void setDualSelectedCategories(
			DualListModel<Category> dualSelectedCategories) {
		this.dualSelectedCategories = dualSelectedCategories;
	}
	
}

