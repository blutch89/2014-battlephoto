package ch.blutch.battlephoto.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.impl.GenericDaoImpl;
import ch.blutch.battlephoto.model.dao.impl.UserDaoImpl;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.entity.User;
import ch.blutch.battlephoto.model.impl.UserGrade;
import ch.blutch.battlephoto.model.service.UserService;

@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl extends GenericDaoImpl<User> implements UserService {

	@Autowired
	private UserDaoImpl userDao;
	
	public UserServiceImpl() {
		super(User.class);
	}

	@Override
	public List<User> findBestPhotographers() {
		return userDao.findBestPhotographers();
	}

	@Override
	public List<User> findFirstBestPhotographers() {
		return userDao.findFirstBestPhotographers();
	}
	
	@Override
	public List<Photo> findPhotosUser(int userId) {
		return userDao.findPhotosUser(userId);
	}

	@Override
	public boolean isUsernameDisponible(String username) {
		return userDao.isUsernameDisponible(username);
	}

	@Override
	public boolean isEmailDisponible(String email) {
		return userDao.isEmailDisponible(email);
	}

	@Override
	public User getUserFromUsername(String username) {
		return userDao.getUserFromUsername(username);
	}

	@Override
	public int getNbPhotosUser(int userId) {
		return userDao.getNbPhotosUser(userId);
	}

	@Override
	public User getUserFromEmail(String email) {
		return userDao.getUserFromEmail(email);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateUserScore(int userId) {
		userDao.updateUserScore(userId);
	}
	
}
