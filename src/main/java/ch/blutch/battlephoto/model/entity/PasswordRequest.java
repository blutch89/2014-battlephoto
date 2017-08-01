package ch.blutch.battlephoto.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "password_requests")
public class PasswordRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "INT UNSIGNED")
	private int id;
	
	@Column(name = "request_id", columnDefinition = "VARCHAR(50)", nullable = true)
	private String requestId;
	
	@Column(name = "creation_date", columnDefinition = "DATETIME", nullable = true)
	private Date creationDate;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", columnDefinition = "MEDIUMINT UNSIGNED", nullable = true)
	private User userRequested;
	
	public PasswordRequest() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getUserRequested() {
		return userRequested;
	}

	public void setUserRequested(User userRequested) {
		this.userRequested = userRequested;
	}
	
}
