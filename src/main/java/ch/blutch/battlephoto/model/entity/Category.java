package ch.blutch.battlephoto.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="categories", indexes = {
					@Index(name="ind_description_en", columnList="description_en"),
					@Index(name="ind_description_fr", columnList="description_fr"),
					@Index(name="ind_description_de", columnList="description_de"),
					@Index(name="ind_description_it", columnList="description_it"),
					@Index(name="ind_description_es", columnList="description_es"),
					@Index(name="ind_parent_category_id", columnList="parent_category_id")})
public class Category implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "SMALLINT UNSIGNED")
	private int id;
	
	@Column(name = "description_en", columnDefinition = "VARCHAR(60)", nullable = true)
	private String descriptionEn;
	
	@Column(name = "description_fr", columnDefinition = "VARCHAR(60)", nullable = true)
	private String descriptionFr;
	
	@Column(name = "description_de", columnDefinition = "VARCHAR(60)", nullable = true)
	private String descriptionDe;
	
	@Column(name = "description_it", columnDefinition = "VARCHAR(60)", nullable = true)
	private String descriptionIt;
	
	@Column(name = "description_es", columnDefinition = "VARCHAR(60)", nullable = true)
	private String descriptionEs;
	
	@Column(name="orders", columnDefinition = "TINYINT UNSIGNED", nullable = true)
	private int order;
	
	@ManyToMany(mappedBy = "categories")
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<Photo> photos = new HashSet<>(0);
	
	@ManyToOne(targetEntity = Category.class)
	@JoinColumn(name = "parent_category_id", columnDefinition = "SMALLINT UNSIGNED", nullable = true)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Category parentCategory;
	
	@OneToMany(targetEntity = Category.class, mappedBy = "parentCategory")
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<Category> subCategories = new HashSet<>(0);
	
	public Category() {
		
	}
	
	public boolean isParent() {
		return parentCategory == null ? true : false;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescriptionFr() {
		return descriptionFr;
	}

	public void setDescriptionFr(String descriptionFr) {
		this.descriptionFr = descriptionFr;
	}
	
	public Set<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public String getDescriptionDe() {
		return descriptionDe;
	}

	public void setDescriptionDe(String descriptionDe) {
		this.descriptionDe = descriptionDe;
	}

	public String getDescriptionIt() {
		return descriptionIt;
	}

	public void setDescriptionIt(String descriptionIt) {
		this.descriptionIt = descriptionIt;
	}

	public String getDescriptionEs() {
		return descriptionEs;
	}

	public void setDescriptionEs(String descriptionEs) {
		this.descriptionEs = descriptionEs;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	public Set<Category> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Set<Category> subCategories) {
		this.subCategories = subCategories;
	}
	
}
