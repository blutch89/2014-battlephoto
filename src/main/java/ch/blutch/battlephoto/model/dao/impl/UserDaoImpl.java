package ch.blutch.battlephoto.model.dao.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.UserDao;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.entity.User;

@Repository("userDao")
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {
	
	@Autowired
	private PhotoDaoImpl photoDao;
	
	public UserDaoImpl() {
		super(User.class);
	}
	
	@Override
	public List<User> findBestPhotographers() {
		List<User> users = getCurrentSession().createCriteria(User.class)
			.addOrder(Order.desc("score"))
			.addOrder(Order.asc("username"))
			.list();
		
//		List<User> users = findAll();
//		
//		Collections.sort(users, new Comparator<User>() {
//
//			@Override
//			public int compare(User u1, User u2) {
//				int compare = u2.getScore() - u1.getScore();
//				
//				// Si 2 user ont le même score, retourne par ordre alphabétique
//				if (compare == 0) {
//					return u2.getUsername().compareTo(u1.getUsername());
//				}
//				
//				return compare;
//			}
//		});
		
		return users;
	}
	
	public List<User> findFirstBestPhotographers() {
		List<User> firstBestPhotographers = getCurrentSession().createCriteria(User.class)
				.addOrder(Order.desc("score"))
				.addOrder(Order.asc("username"))
				.setMaxResults(10)
				.list();
		
		
//		List<User> firstBestPhotographers = findBestPhotographers();
//		
//		if (firstBestPhotographers.size() > 10) {
//			firstBestPhotographers = firstBestPhotographers.subList(0, 10);
//		}
		
		return firstBestPhotographers;
	}
	
	public List<Photo> findPhotosUser(int userId) {
		List<Photo> photos = getCurrentSession().createCriteria(Photo.class)
				.add(Restrictions.eq("owner.id", userId))
				.addOrder(Order.desc("creationDate")).list();
		
		return photos;
	}

	@Override
	public boolean isUsernameDisponible(String username) {
//		Query q = getCurrentSession().createQuery("SELECT u FROM User u WHERE u.username = :username");
//		q.setString("username", username);
//		int nbResults = q.list().size();
		
		int nbResults = ((Long) getCurrentSession().createCriteria(User.class)
			.add(Restrictions.eq("username", username))
			.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return nbResults == 0 ? true : false;
	}

	@Override
	public boolean isEmailDisponible(String email) {
//		Query q = getCurrentSession().createQuery("SELECT u FROM User u WHERE u.email = :email");
//		q.setString("email", email);
//		int nbResults = q.list().size();
		
		int nbResults = ((Long) getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("email", email))
				.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return nbResults == 0 ? true : false;
	}

	@Override
	public User getUserFromUsername(String username) {
//		Query q = getCurrentSession().createQuery("SELECT u FROM User u WHERE u.username = :username");
//		q.setString("username", username);
//		User user = (User) q.uniqueResult();
		
		User user = (User) getCurrentSession().createCriteria(User.class)
			.add(Restrictions.eq("username", username))
			.uniqueResult();
		
		return user;
	}

	@Override
	public int getNbPhotosUser(int userId) {
		List<Photo> userPhotos = findPhotosUser(userId);
		
		return userPhotos.size();
	}

	@Override
	public User getUserFromEmail(String email) {
		User user = (User) getCurrentSession().createCriteria(User.class)
			.add(Restrictions.eq("email", email))
			.uniqueResult();
		
		return user;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateUserScore(int userId) {
		User user = findById(userId);
		
		List<Photo> photos = findPhotosUser(userId);
		int score = 0;
		
		for (Photo photo : photos) {
			score += photo.getScore();
		}
		
		user.setScore(score);
		
		save(user);
	}

}
