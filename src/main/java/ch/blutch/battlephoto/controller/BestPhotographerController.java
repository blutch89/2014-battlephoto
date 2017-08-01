package ch.blutch.battlephoto.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.datatable.DataTable;
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
public class BestPhotographerController {

	@Autowired
	private CategoryServiceImpl categoryService;
	
	@Autowired
	private PhotoServiceImpl photoService;
	
	@Autowired
	private UserServiceImpl userService;
	
	private List<User> bestPhotographers;
	private int selectedUserId;
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
		
		// Charge les photographes
		bestPhotographers = userService.findBestPhotographers();
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		if (UserUtil.isLogged()) {
			selectedUserId = userService.getUserFromUsername(UserUtil.getUsername()).getId();
		} else {
			selectedUserId = 0;
		}
	}
	
	public int getNbPhotosUser(int userId) {
		List<Photo> photos = userService.findPhotosUser(userId);
		
		return photos.size();
	}

	public Integer getSelectedUserId() {
		return selectedUserId;
	}

	public void setSelectedUserId(Integer selectedUserId) {
		this.selectedUserId = selectedUserId;
	}

	public List<User> getBestPhotographers() {
		return bestPhotographers;
	}

	public void setBestPhotographers(List<User> bestPhotographers) {
		this.bestPhotographers = bestPhotographers;
	}
	
}
