package ch.blutch.battlephoto.model.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.PasswordRequestDao;
import ch.blutch.battlephoto.model.entity.PasswordRequest;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.entity.User;

@Repository("passwordRequestDao")
public class PasswordRequestDaoImpl extends GenericDaoImpl<PasswordRequest> implements PasswordRequestDao {

	@Autowired
	private UserDaoImpl userDao;
	
	public PasswordRequestDaoImpl() {
		super(PasswordRequest.class);
	}

	@Override
	public User findUserFromRequestId(String requestId) {
		PasswordRequest passwordRequest = (PasswordRequest) getCurrentSession().createCriteria(PasswordRequest.class)
			.add(Restrictions.eq("requestId", requestId))
			.uniqueResult();
		
		User user = passwordRequest.getUserRequested();
		
		return user;
	}

	@Override
	public PasswordRequest findPasswordRequestFromRequestId(String requestId) {
		PasswordRequest passwordRequest = (PasswordRequest) getCurrentSession().createCriteria(PasswordRequest.class)
			.add(Restrictions.eq("requestId", requestId))
			.uniqueResult();
		
		return passwordRequest;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteOldThan24H() {
		getCurrentSession().createQuery("DELETE FROM PasswordRequest pr WHERE DATEDIFF(CURDATE(), pr.creationDate) > 1").executeUpdate();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRequestsFromUser(int userId) {
		getCurrentSession().createQuery("DELETE FROM PasswordRequest pr WHERE pr.userRequested.id = :userId")
			.setInteger("userId", userId)
			.executeUpdate();
	}

	
}
