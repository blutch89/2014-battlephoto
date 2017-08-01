package ch.blutch.battlephoto.model.service;

import ch.blutch.battlephoto.model.entity.PasswordRequest;
import ch.blutch.battlephoto.model.entity.User;

public interface PasswordRequestService {

	public User findUserFromRequestId(String requestId);
	
	public PasswordRequest findPasswordRequestFromRequestId(String requestId);
	
	public void deleteOldThan24H();
	
	public void deleteRequestsFromUser(int userId);
	
}
