package ch.blutch.battlephoto.model.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import ch.blutch.battlephoto.controller.utils.DateUtil;

@Entity
@Table(name = "photos", indexes = {
							@Index(name = "ind_user_id", columnList = "user_id")
							})
public class Photo implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "INT UNSIGNED")
	private int id;
	
	@Column(name = "description", columnDefinition = "VARCHAR(150)", nullable = true)
	private String description;
	
	@Column(name = "file_name", columnDefinition = "VARCHAR(100)", nullable = false)
	private String fileName;
	
	@Column(name = "score", columnDefinition = "MEDIUMINT UNSIGNED", nullable = false)
	private int score;
	
	@Column(name = "creation_date", columnDefinition = "DATETIME", nullable = true)
	private Date creationDate;
	
	@Column(name = "capture_date", columnDefinition = "DATETIME", nullable = true)
	private Date captureDate;
	
	@ManyToMany
	@JoinTable(name = "categories_photos",
			indexes = {@Index(name = "ind_photo_id", columnList = "photo_id"),
						@Index(name = "ind_category_id", columnList = "category_id")},
	        joinColumns = @JoinColumn(name = "photo_id", columnDefinition = "INT UNSIGNED"),
	        inverseJoinColumns = @JoinColumn(name = "category_id", columnDefinition = "SMALLINT UNSIGNED"))
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<Category> categories = new HashSet<>(0);
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", columnDefinition = "MEDIUMINT UNSIGNED", nullable = true)
	@Cascade({CascadeType.SAVE_UPDATE})
	private User owner;
	
	@OneToMany(targetEntity = Note.class, mappedBy = "photo")
	@Cascade({CascadeType.ALL})
	private Set<Note> notes = new HashSet<>(0);
	
	public Photo() {
		
	}
	
	public String formatCreationDate() {
		return DateUtil.swissDate.format(this.creationDate);
	}
	
	public String formatCaptureDate() {
		return DateUtil.swissDate.format(this.captureDate);
	}
	
	public int getNbDaysSinceCreation() {
		Date now = new Date();
		long between = now.getTime() - creationDate.getTime();
		int nbDay = (int) Math.ceil(between / 1000.0 / 60.0 / 60.0 / 24.0);
		
		return nbDay;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<Note> getNotes() {
		return notes;
	}

	public void setNotes(Set<Note> notes) {
		this.notes = notes;
	}

	public Date getCaptureDate() {
		return captureDate;
	}

	public void setCaptureDate(Date captureDate) {
		this.captureDate = captureDate;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
}
