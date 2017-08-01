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
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.controller.utils.HashUtil;
import ch.blutch.battlephoto.controller.utils.StringUtil;
import ch.blutch.battlephoto.model.entity.PasswordRequest;
import ch.blutch.battlephoto.model.entity.User;
import ch.blutch.battlephoto.model.service.impl.GenericServiceImpl;
import ch.blutch.battlephoto.model.service.impl.PasswordRequestServiceImpl;
import ch.blutch.battlephoto.model.service.impl.UserServiceImpl;
import ch.blutch.battlephoto.model.utils.MailUtil;

@ManagedBean
@RequestScoped
@Controller
@Scope("request")
public class ForgetPasswordController {

	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private PasswordRequestServiceImpl passwordRequestService;
	
	// Form param
	private String username;
	private String email;
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	// Appelé pour envoyer le link de password request par mail
	public void createAndSendPasswordRequest() {
		// Si l'email a été spécifiée, continue avec l'email
		if (!email.equals("")) {
			// Test si l'email est inscrite dans la base de données
			if (!userService.isEmailDisponible(email)) {
				User user = userService.getUserFromEmail(email);
				
				String recoverLink = createPasswordRequest(user);
				sendLinkMail(email, recoverLink);
			} else {
				FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Cette adresse email n'est pas existante.", "Cette adresse email n'est pas existante.");
				FacesContext.getCurrentInstance().addMessage("form:email", facesMessage);
				
				return;
			}
		} else if (!username.equals("")) {	// Si le username a été spécifié, continue avec le username
			// Test si le username est inscrit dans la base de données
			if (!userService.isUsernameDisponible(username)) {
				User user = userService.getUserFromUsername(username);
				
				String recoverLink = createPasswordRequest(user);
				sendLinkMail(user.getEmail(), recoverLink);
			} else {
				FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Cet utilisateur n'est pas existant.", "Cet utilisateur n'est pas existant.");
				FacesContext.getCurrentInstance().addMessage("form:user", facesMessage);
				
				return;
			}
		} else {
			FacesMessage facesMessageUser = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ce champs est requis.", "Ce champs est requis.");
			FacesContext.getCurrentInstance().addMessage("form:user", facesMessageUser);
			
			FacesMessage facesMessageEmail = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ce champs est requis.", "Ce champs est requis.");
			FacesContext.getCurrentInstance().addMessage("form:email", facesMessageEmail);
			
			return;
		}
		
		// Redirige l'utilisateur
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect("/" + ec.getContextName().toLowerCase() + "/message/forget-password-link-sent");
		} catch (IOException e) {
			try {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				ec.redirect("/" + ec.getContextName().toLowerCase() + "/error/navigation-error");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private String createPasswordRequest(User user) {
		String contextName = FacesContext.getCurrentInstance().getExternalContext().getContextName().toLowerCase();
		String hostName = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
		String recoverId = StringUtil.generateRandomString(20);
		String recoverLink = "http://" + hostName + ":8080/" + contextName + "/password-recovery/" + recoverId;
		
		PasswordRequest request = new PasswordRequest();
		request.setCreationDate(new Date());
		request.setRequestId(recoverId);
		request.setUserRequested(user);
		
		passwordRequestService.save(request);
		
		return recoverLink;
	}
	
	private void sendLinkMail(String email, String recoverLink) {
		try {
			MailUtil.sendPasswordRequestMail(email, recoverLink);
		} catch (MessagingException e) {
			try {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				ec.redirect("/" + ec.getContextName().toLowerCase() + "/error/navigation-error");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
