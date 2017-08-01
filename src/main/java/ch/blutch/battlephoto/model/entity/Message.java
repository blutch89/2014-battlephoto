package ch.blutch.battlephoto.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="messages")
public class Message {

	@Id
	@Column(name = "message_id", columnDefinition = "VARCHAR(50)", nullable = false)
	private String messageId;
	
	@Column(name = "title", columnDefinition = "VARCHAR(150)", nullable = false)
	private String title;
	
	@Column(name = "message", columnDefinition = "VARCHAR(250)", nullable = false)
	private String message;
	
	@Column(name = "destination_link", columnDefinition = "VARCHAR(50)", nullable = false)
	private String destinationLink;
	
	@Column(name = "message_link", columnDefinition = "VARCHAR(150)", nullable = false)
	private String messageLink;
	
	@Column(name="is_fatal", columnDefinition = "BOOLEAN", nullable = true)
	private boolean isFatal = false;
	
	public Message() {
		
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDestinationLink() {
		return destinationLink;
	}

	public void setDestinationLink(String destinationLink) {
		this.destinationLink = destinationLink;
	}

	public String getMessageLink() {
		return messageLink;
	}

	public void setMessageLink(String messageLink) {
		this.messageLink = messageLink;
	}

	public boolean isFatal() {
		return isFatal;
	}

	public void setFatal(boolean isFatal) {
		this.isFatal = isFatal;
	}
	
}
