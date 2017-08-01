package ch.blutch.battlephoto.model.service;

import java.util.List;

import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.entity.User;
import ch.blutch.battlephoto.model.impl.UserGrade;

public interface UserService {

	public List<User> findBestPhotographers();
	
	public List<User> findFirstBestPhotographers();
	
	public List<Photo> findPhotosUser(int userId);
	
	public int getNbPhotosUser(int userId);
	
	public void updateUserScore(int userId);
	
	public boolean isUsernameDisponible(String username);
	
	public boolean isEmailDisponible(String email);
	
	public User getUserFromUsername(String username);
	
	public User getUserFromEmail(String email);
	
}
