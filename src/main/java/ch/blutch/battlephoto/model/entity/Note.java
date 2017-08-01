package ch.blutch.battlephoto.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "notes", indexes = {@Index(name = "ind_user_id", columnList = "user_id"),
								@Index(name = "ind_photo_id", columnList = "photo_id")})
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "INT UNSIGNED")
	private int id;
	
	@Column(name = "note", columnDefinition = "TINYINT UNSIGNED", nullable = false)
	private int note;
	
	@Column(name = "ip_address", columnDefinition = "VARCHAR(25)", nullable = true)
	private String ipAddress;
	
	@Column(name = "date_of_note", columnDefinition = "DATETIME", nullable = true)
	private Date dateOfNote;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", columnDefinition = "MEDIUMINT UNSIGNED")
	@Cascade({CascadeType.SAVE_UPDATE})
	private User user;
	
	@ManyToOne(targetEntity = Photo.class)
	@JoinColumn(name = "photo_id", columnDefinition = "INT UNSIGNED")
	@Cascade({CascadeType.SAVE_UPDATE})
	private Photo photo;
	
	public Note() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getDateOfNote() {
		return dateOfNote;
	}

	public void setDateOfNote(Date dateOfNote) {
		this.dateOfNote = dateOfNote;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
}
