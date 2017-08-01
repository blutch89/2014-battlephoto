package ch.blutch.battlephoto.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.controller.utils.HashUtil;
import ch.blutch.battlephoto.model.entity.PasswordRequest;
import ch.blutch.battlephoto.model.entity.User;
import ch.blutch.battlephoto.model.service.impl.PasswordRequestServiceImpl;
import ch.blutch.battlephoto.model.service.impl.UserServiceImpl;

@ManagedBean
@RequestScoped
@Controller
@Scope("request")
public class PasswordRecoveryController {

	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private PasswordRequestServiceImpl passwordRequestService;
	
	// Password Recovery
	private String requestId;
	private String password1;
	private String password2;
	private String formClassPassword1 = "";
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
		
		deleteOldPasswordRequest();	// Supprime les anciennes requêtes
		testIfExist();				// Test si le requestId existe
		testIfOutOfDate();			// Test si request pas hors date
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		if (requestParameters.get("requestId") == null) {
			requestId = null;
		} else {
			requestId = requestParameters.get("requestId");
		}
	}
	
	private void deleteOldPasswordRequest() {
		passwordRequestService.deleteOldThan24H();
	}
	
	private void testIfOutOfDate() {
		// Test si le délai de 24h n'est pas dépassé
		PasswordRequest passwordRequest = passwordRequestService.findPasswordRequestFromRequestId(requestId);
		
		Date requestDate = passwordRequest.getCreationDate();
		Date nowDate = new Date();
		
		long msBetween = nowDate.getTime() - requestDate.getTime();
		int nbHourBetween = (int) (msBetween / 1000 / 60 / 60);
		
		// Si le délai est dépassé
		if (nbHourBetween >= 24) {
			// Redirige l'utilisateur
			try {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				ec.redirect("/" + ec.getContextName().toLowerCase() + "/message/password-recovery-out-of-date");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void testIfExist() {
		PasswordRequest passwordRequest = passwordRequestService.findPasswordRequestFromRequestId(requestId);
		
		if (passwordRequest == null) {
			// Redirige l'utilisateur
			try {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				ec.redirect("/" + ec.getContextName().toLowerCase() + "/message/password-recovery-not-exist");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Appelé lors du password recovery
	public void modifyPassword() {
		// Si les 2 mots de passes ne correspondent pas
		if (! password1.equals(password2)) {
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les mots de passes ne correspondent pas","Les mots de passes ne correspondent pas");
			FacesContext.getCurrentInstance().addMessage("form:password1", facesMessage);
			formClassPassword1 = "has-error";
			
			return;
		}
		
		// Récupère le user d'après le requestId
		User user = passwordRequestService.findUserFromRequestId(requestId);
		
		// Modifie le password
		user.setPassword(HashUtil.getHash(password1));
		userService.saveOrUpdate(user);
		
		// Supprime les passwordrequest de ce user
		passwordRequestService.deleteRequestsFromUser(user.getId());
		
		// Redirige l'utilisateur
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect("/" + ec.getContextName().toLowerCase() + "/message/password-recovery-success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getFormClassPassword1() {
		return formClassPassword1;
	}

	public void setFormClassPassword1(String formClassPassword1) {
		this.formClassPassword1 = formClassPassword1;
	}
	
}
