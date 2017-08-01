package ch.blutch.battlephoto.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.controller.utils.ViewParameters;
import ch.blutch.battlephoto.model.entity.Category;
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
public class TemplateController {

	@Autowired
	private CategoryServiceImpl categoryService;
	
	@Autowired
	private PhotoServiceImpl photoService;
	
	@Autowired
	private UserServiceImpl userService;
	
	// Sert à indiquer la position du photographe en cours
	private int photographerIndex;
	
	private UserGrade userGrade;
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
	}
	
	private void loadParameters() {
		
	}
	
	public int getCurrentPhotographerIndex() {
		photographerIndex += 1;
		
		return photographerIndex;
	}
	
	public int findNbPhotosInCategoryAndSubCategories(int id) {
		int nbPhotos = categoryService.findNbPhotosInCategoryAndSubCategories(id);
		
		return nbPhotos;
	}

	// Utile pour le menu de liste de catégories
	public List<Category> findParentsCategories() {
		List<Category> parentsCategories = categoryService.findParentsCategories();
		
		return parentsCategories;
	}
	
	public List<User> findFirstBestPhotographers() {
		List<User> firstBestPhotographers = userService.findFirstBestPhotographers();
		
		return firstBestPhotographers;
	}
	
	public List<Photo> findFirstBestPhotos() {
		List<Photo> firstBestPhotos = photoService.findXBestPhotos(0, true);
		
		return firstBestPhotos;
	}

	public int getPhotographerIndex() {
		return photographerIndex;
	}

	public void setPhotographerIndex(int photographerIndex) {
		this.photographerIndex = photographerIndex;
	}

	public UserGrade getUserGrade() {
		return userGrade;
	}

	public void setUserGrade(UserGrade userGrade) {
		this.userGrade = userGrade;
	}
	
}
