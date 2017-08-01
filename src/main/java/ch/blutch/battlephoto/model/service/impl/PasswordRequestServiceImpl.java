package ch.blutch.battlephoto.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.impl.PasswordRequestDaoImpl;
import ch.blutch.battlephoto.model.entity.PasswordRequest;
import ch.blutch.battlephoto.model.entity.User;
import ch.blutch.battlephoto.model.service.PasswordRequestService;

@Service("passwordRequestService")
@Transactional(readOnly = true)
public class PasswordRequestServiceImpl extends GenericServiceImpl<PasswordRequest> implements PasswordRequestService {

	@Autowired
	private PasswordRequestDaoImpl passwordRequestDao;
	
	@Override
	public User findUserFromRequestId(String requestId) {
		return passwordRequestDao.findUserFromRequestId(requestId);
	}

	@Override
	public PasswordRequest findPasswordRequestFromRequestId(String requestId) {
		return passwordRequestDao.findPasswordRequestFromRequestId(requestId);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteOldThan24H() {
		passwordRequestDao.deleteOldThan24H();
	}

	@Override
	public void deleteRequestsFromUser(int userId) {
		passwordRequestDao.deleteRequestsFromUser(userId);
	}
	
	// !!!! ne pas oublier les transactions à mettre dans les services uniquement
	
}
