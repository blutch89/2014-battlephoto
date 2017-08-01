package ch.blutch.battlephoto.model.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import ch.blutch.battlephoto.controller.utils.DateUtil;
import ch.blutch.battlephoto.model.impl.UserGrade;

@Entity
@Table(name = "users", indexes = {
							@Index(name = "ind_username", columnList = "username"),
							@Index(name = "ind_user_role_id", columnList = "user_role_id")},
							uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "MEDIUMINT UNSIGNED")
	private int id;
	
	@Column(name = "username", columnDefinition = "VARCHAR(100)", nullable = false)
	private String username;
	
	@Column(name = "email", columnDefinition = "VARCHAR(150)", nullable = false)
	private String email;
	
	@Column(name = "password", columnDefinition = "VARCHAR(500)", nullable = false)
	private String password;
	
	@Column(name = "enabled", columnDefinition = "BOOLEAN", nullable = true)
	private boolean enabled;
	
	@Column(name = "score", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
	private int score;
	
	@Transient
	private UserGrade userGrade = new UserGrade(score);
	
	@Column(name = "creation_date", columnDefinition = "DATETIME", nullable = true)
	private Date creationDate;
	
	@OneToMany(targetEntity = Photo.class, mappedBy = "owner")
	@Cascade({CascadeType.ALL})
	private Set<Photo> photos = new HashSet<>(0);
	
	@OneToMany(targetEntity = PasswordRequest.class, mappedBy="userRequested")
	@Cascade({CascadeType.ALL})
	private Set<PasswordRequest> passwordRequests = new HashSet<>(0);
	
	@ManyToOne(targetEntity = UserRole.class)
	@JoinColumn(name = "user_role_id", columnDefinition = "SMALLINT UNSIGNED", nullable = true)
	@Cascade({CascadeType.SAVE_UPDATE})
	private UserRole userRole;
	
	@OneToMany(targetEntity = Note.class, mappedBy = "user")
	@Cascade({CascadeType.ALL})
	private Set<Note> notes = new HashSet<>(0);
	
	public User() {
		
	}
	
	public String formatCreationDate() {
		return DateUtil.swissDate.format(this.creationDate);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Set<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public Set<Note> getNotes() {
		return notes;
	}

	public void setNotes(Set<Note> notes) {
		this.notes = notes;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Set<PasswordRequest> getPasswordRequests() {
		return passwordRequests;
	}

	public void setPasswordRequests(Set<PasswordRequest> passwordRequests) {
		this.passwordRequests = passwordRequests;
	}

	public UserGrade getUserGrade() {
		userGrade = new UserGrade(score);
		
		return userGrade;
	}

	public void setUserGrade(UserGrade userGrade) {
		this.userGrade = userGrade;
	}
}
