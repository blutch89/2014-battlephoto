package ch.blutch.battlephoto.model.dao;

import ch.blutch.battlephoto.model.entity.PasswordRequest;
import ch.blutch.battlephoto.model.entity.User;

public interface PasswordRequestDao {

	// Mettre les méthodes spécifiques dans cette partie
	public User findUserFromRequestId(String requestId);
	
	public PasswordRequest findPasswordRequestFromRequestId(String requestId);
	
	public void deleteOldThan24H();
	
	public void deleteRequestsFromUser(int userId);
	
}
