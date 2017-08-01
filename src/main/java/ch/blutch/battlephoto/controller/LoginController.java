package ch.blutch.battlephoto.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.controller.utils.HashUtil;
import ch.blutch.battlephoto.controller.utils.UserUtil;
import ch.blutch.battlephoto.model.entity.User;
import ch.blutch.battlephoto.model.entity.UserRole;
import ch.blutch.battlephoto.model.service.GenericService;
import ch.blutch.battlephoto.model.service.impl.CategoryServiceImpl;
import ch.blutch.battlephoto.model.service.impl.GenericServiceImpl;
import ch.blutch.battlephoto.model.service.impl.PhotoServiceImpl;
import ch.blutch.battlephoto.model.service.impl.UserServiceImpl;

@ManagedBean
@RequestScoped
@Controller
@Scope("request")
public class LoginController {

	@Autowired
	private CategoryServiceImpl categoryService;
	
	@Autowired
	private PhotoServiceImpl photoService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private GenericServiceImpl<UserRole> userRoleService;
	
	// Paramètres
	private User newUser;
	private User myUser;
	
	private String password;
	private String password2;
	private String formClassUsername = "";
	private String formClassEmail = "";
	private String formClassPassword2 = "";
	
	private UIComponent password2Component;
	
	@PostConstruct
	public void postConstruct() {
		userRoleService.setClazz(UserRole.class);
		
		loadParameters();
		
		// Inscription
		newUser = new User();
		newUser.setEnabled(true);
		newUser.setUserRole(userRoleService.findById(UserRole.ROLE_USER));
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		// Param myUser
		myUser = userService.getUserFromUsername(UserUtil.getUsername());
	}
	
	// Appelé pour le login
	public void doLogin() {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
		    ExternalContext extenalContext = facesContext.getExternalContext();
		    RequestDispatcher dispatcher = ((ServletRequest)extenalContext.getRequest()).getRequestDispatcher("/j_spring_security_check");
			dispatcher.forward((ServletRequest)extenalContext.getRequest(), (ServletResponse)extenalContext.getResponse());
			facesContext.responseComplete();
		} catch (ServletException | IOException e) {
			// TODO LoginController/doLogin() : gérer en cas d'erreur
			e.printStackTrace();
		}	    
	}
	
	// Appelé pour l'inscription
	public void signUp () {
		// Si les 2 mots de passes ne correspondent pas
		if (! newUser.getPassword().equals(password2)) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les mots de passes ne correspondent pas","Les mots de passes ne correspondent pas");
			context.addMessage(password2Component.getClientId(context), facesMessage);
			formClassPassword2 = "has-error";
			
			return;
		}
		
		// Inscris l'utilisateur
		newUser.setPassword(HashUtil.getHash(newUser.getPassword()));
		newUser.setCreationDate(new Date());
		userService.saveOrUpdate(newUser);
		
		// Redirige l'utilisateur
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect("/" + ec.getContextName().toLowerCase() + "/message/signup-success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Appelé pour modifier le profil
	public void modifyUser() {
		// Si l'utilisateur veut changer son mot de passe
		if (!password.equals("") && !password2.equals("")) {
			// Si les 2 mots de passes ne correspondent pas
			if (!password.equals(password2)) {
				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les mots de passes ne correspondent pas","Les mots de passes ne correspondent pas");
				context.addMessage(password2Component.getClientId(context), facesMessage);
				formClassPassword2 = "has-error";
				
				return;
			} else {
				myUser.setPassword(HashUtil.getHash(password));
			}
		}
		
		// Enregistre les modifications
		userService.saveOrUpdate(myUser);
		
		// Redirige l'utilisateur
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect("/" + ec.getContextName().toLowerCase() + "/message/myprofile-edit-success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void validateUsername(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (! userService.isUsernameDisponible((String)value)) {
			Collection<FacesMessage> messages = new ArrayList<>();
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Non disponible","Non disponible");
			messages.add(facesMessage);
			context.addMessage("user", facesMessage);
			formClassUsername = "has-error";
			
			throw new ValidatorException(messages);
		} else {
			formClassUsername = "has-success";
		}
	}
	
	public void validateEmail(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (!userService.isEmailDisponible((String)value)) {
			Collection<FacesMessage> messages = new ArrayList<>();
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Non disponible","Non disponible");
			messages.add(facesMessage);
			context.addMessage("email", facesMessage);
			formClassEmail = "has-error";
			
			throw new ValidatorException(messages);
		} else if (!UserUtil.isValidEmail((String)value)) {
			Collection<FacesMessage> messages = new ArrayList<>();
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Veuillez entrer une adresse email valide","Veuillez entrer une adresse email valide");
			messages.add(facesMessage);
			context.addMessage("email", facesMessage);
			formClassEmail = "has-error";
			
			throw new ValidatorException(messages);
		} else {
			formClassEmail = "has-success";
		}
	}
	
	public void validateEmailEdit(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// Si l'email n'a pas été changée
		if (userService.getUserFromUsername(UserUtil.getUsername()).getEmail().equals(value)) {
			return;
		}
		
		if (!userService.isEmailDisponible((String)value)) {
			Collection<FacesMessage> messages = new ArrayList<>();
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Non disponible","Non disponible");
			messages.add(facesMessage);
			context.addMessage("email", facesMessage);
			formClassEmail = "has-error";
			
			throw new ValidatorException(messages);
		} else if (!UserUtil.isValidEmail((String)value)) {
			Collection<FacesMessage> messages = new ArrayList<>();
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Veuillez entrer une adresse email valide","Veuillez entrer une adresse email valide");
			messages.add(facesMessage);
			context.addMessage("email", facesMessage);
			formClassEmail = "has-error";
			
			throw new ValidatorException(messages);
		} else {
			formClassEmail = "has-success";
		}
	}
	
	public String getUsername() {
		return UserUtil.getUsername();
	}
	
	public boolean isLogged() {
		return UserUtil.isLogged();
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getFormClassUsername() {
		return formClassUsername;
	}

	public void setFormClassUsername(String formClassUsername) {
		this.formClassUsername = formClassUsername;
	}

	public String getFormClassEmail() {
		return formClassEmail;
	}

	public void setFormClassEmail(String formClassEmail) {
		this.formClassEmail = formClassEmail;
	}

	public String getFormClassPassword2() {
		return formClassPassword2;
	}

	public void setFormClassPassword2(String formClassPassword2) {
		this.formClassPassword2 = formClassPassword2;
	}

	public UIComponent getPassword2Component() {
		return password2Component;
	}

	public void setPassword2Component(UIComponent password2Component) {
		this.password2Component = password2Component;
	}

	public User getMyUser() {
		return myUser;
	}

	public void setMyUser(User myUser) {
		this.myUser = myUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
