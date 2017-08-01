package ch.blutch.battlephoto.controller;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.model.entity.Message;
import ch.blutch.battlephoto.model.service.impl.GenericServiceImpl;
import ch.blutch.battlephoto.model.service.impl.MessageServiceImpl;

@ManagedBean
@RequestScoped
@Controller
@Scope("request")
public class MessageController {

	@Autowired
	private MessageServiceImpl messageService;
	
	// Param
	private String messageId;
	private Message message;
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		if (requestParameters.get("messageId") == null) {
			messageId = null;
			message = null;
		} else {
			messageId = requestParameters.get("messageId");
			message = messageService.findMessageById(messageId);
		}
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
}
