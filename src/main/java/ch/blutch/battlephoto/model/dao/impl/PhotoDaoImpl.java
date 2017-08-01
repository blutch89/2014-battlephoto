package ch.blutch.battlephoto.model.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.PhotoDao;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Note;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.utils.ModelParameters;

@Repository("photoDao")
public class PhotoDaoImpl extends GenericDaoImpl<Photo> implements PhotoDao {

	@Autowired
	private GenericDaoImpl<Note> genericDao;
	
	public PhotoDaoImpl() {
		super(Photo.class);
	}

	@Override
	public List<Photo> findXRandomPhotos(List<Photo> actualPhotosList) {
		if (actualPhotosList.size() > 0) {
			// Crée le not in
			List<Integer> ids = new ArrayList<>();
			
			for (Photo photo : actualPhotosList) {
				ids.add(photo.getId());
			}
			
			// Crée la requête
			List list = getCurrentSession().createCriteria(Photo.class)
					.add(Restrictions.not(Restrictions.in("id", ids)))
					.add(Restrictions.sqlRestriction("1=1 order by rand()"))
					.setMaxResults(ModelParameters.slideShowNbPhotosToRefresh)
					.list();
			
			return list;
		} else {
			List list = getCurrentSession().createCriteria(Photo.class)
					.add(Restrictions.sqlRestriction("1=1 order by rand()"))
					.setMaxResults(ModelParameters.slideShowNbPhotosToRefresh)
					.list();
			
			return list;
		}
	}

	@Override
	public List<Photo> findXBestPhotos(int index, boolean isForTemplate) {
		int maxResult = isForTemplate == false ? ModelParameters.slideShowNbPhotosToRefresh : ModelParameters.templateNbBestPhotosForRightColumn;
		
		List<Photo> bestPhotos = getCurrentSession().createCriteria(Photo.class)
			.addOrder(Order.desc("score"))
			.addOrder(Order.asc("fileName"))
			.setMaxResults(maxResult)
			.setFirstResult(index)
			.list();
		
		return bestPhotos;
	}

	@Override
	public List<Category> findCategories(int photoId) {
		List<Category> categories = getCurrentSession().createCriteria(Category.class)
				.addOrder(Order.asc("order"))
				.createCriteria("photos")
				.add(Expression.eq("id", photoId))
				.list();
				
		return categories;
	}

	@Override
	public boolean userCanNote(int userId, int photoId) {
		int nb = ((Long) getCurrentSession().createCriteria(Note.class)
			.add(Restrictions.eq("photo.id", photoId))
			.add(Restrictions.eq("user.id", userId))
			.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return nb == 0 ? true : false;
	}

	@Override
	public boolean ipAddressCanNote(String ipAddress, int photoId) {
		int nb = ((Long) getCurrentSession().createCriteria(Note.class)
				.add(Restrictions.eq("photo.id", photoId))
				.add(Restrictions.eq("ipAddress", ipAddress))
				.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return nb == 0 ? true : false;
	}

	@Override
	public int findNbTotalPhotos() {
//		int nbPhotos = ((Long)getCurrentSession().createQuery("SELECT COUNT(p) FROM Photo p").uniqueResult()).intValue();
		int nbPhotos = ((Long) getCurrentSession().createCriteria(Photo.class)
				.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
		
		return nbPhotos;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updatePhotoScore(int photoId) {
		Photo photo = findById(photoId);
//		int nbNote = ((Long)getCurrentSession().createQuery("SELECT COUNT(n) FROM Note n WHERE n.photo.id = " + photoId).uniqueResult()).intValue();
		
		int nbNote = ((Long) getCurrentSession().createCriteria(Note.class)
				.add(Restrictions.eq("photo.id", photoId))
				.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
		
		photo.setScore(nbNote);
		
		save(photo);
	}	
}
