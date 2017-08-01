package ch.blutch.battlephoto.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "user_roles", uniqueConstraints = {
								@UniqueConstraint(columnNames = "description")})
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "SMALLINT UNSIGNED")
	private int id;
	
	@Column(name = "description", columnDefinition = "VARCHAR(100)", nullable = false)
	private String description;
	
	@Column(name = "enabled", columnDefinition = "BOOLEAN", nullable = true)
	private boolean enabled;
	
	@OneToMany(targetEntity = User.class, mappedBy = "userRole")
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<User> users = new HashSet<>(0);
	
	public static final int ROLE_USER = 1;
	
	public UserRole() {
		
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
}
