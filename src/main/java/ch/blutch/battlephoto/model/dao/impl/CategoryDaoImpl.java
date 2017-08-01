package ch.blutch.battlephoto.model.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.query.spi.OrdinalParameterDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.blutch.battlephoto.controller.utils.ViewParameters;
import ch.blutch.battlephoto.model.dao.CategoryDao;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.utils.ModelParameters;

@Repository("categoryDao")
public class CategoryDaoImpl extends GenericDaoImpl<Category> implements CategoryDao {

	@Autowired
	private PhotoDaoImpl photoDao;
	
	public CategoryDaoImpl() {
		super(Category.class);
	}

	@Override
	public List<Category> findSubCategories(int id) {
//		List<Category> subCategories = getCurrentSession().createQuery("SELECT c FROM Category c WHERE c.parentCategory = " + id + " ORDER BY c.order ASC").list();
	
		List<Category> subCategories = getCurrentSession().createCriteria(Category.class)
			.add(Restrictions.eq("parentCategory.id", id))
			.addOrder(Order.asc("order"))
			.list();
		
		return subCategories;
	}
	
	@Override
	public int findNbPhotosInCategoryAndSubCategories(int id) {
		List<Category> subCategories = findSubCategories(id);
		List<Integer> categoriesIds = new ArrayList<>();
		
		categoriesIds.add(id);
		
		for (Category category : subCategories) {
			categoriesIds.add(category.getId());
		}
		
		int nbPhotos = ((Long) getCurrentSession().createCriteria(Photo.class)
			.createAlias("categories", "cat")
			.add(Restrictions.in("cat.id", categoriesIds))
			.setProjection(Projections.countDistinct("id"))
			.uniqueResult()).intValue();

		return nbPhotos;
	}

	@Override
	public List<Photo> findXRandomPhotosFromCategory(int id, List<Photo> actualPhotosList) {
		
		if (actualPhotosList.size() > 0) {
			// Crée le not in photo
			List<Integer> ids = new ArrayList<>();
			
			for (Photo photo : actualPhotosList) {
				ids.add(photo.getId());
			}
			
			// Créé le in category
			List<Category> subCategories = findSubCategories(id);
			List<Integer> categoriesIds = new ArrayList<>();
			
			for (Category category : subCategories) {
				categoriesIds.add(category.getId());
			}
			
			
			// Crée la requête
			List list = getCurrentSession().createCriteria(Photo.class)
					.createAlias("categories", "cat")
					.add(Restrictions.in("cat.id", categoriesIds))
					.add(Restrictions.not(Restrictions.in("id", ids)))
					.add(Restrictions.sqlRestriction("1=1 order by rand()"))
					.setMaxResults(ModelParameters.slideShowNbPhotosToRefresh)
					.list();
			
			return list;
		} else {
			// Créé le in category
			List<Category> subCategories = findSubCategories(id);
			List<Integer> categoriesIds = new ArrayList<>();
			
			categoriesIds.add(id);
			
			for (Category category : subCategories) {
				categoriesIds.add(category.getId());
			}
			
			// Crée la requête
			List list = getCurrentSession().createCriteria(Photo.class)
					.createAlias("categories", "cat")
				.add(Restrictions.in("cat.id", categoriesIds))
				.add(Restrictions.sqlRestriction("1=1 order by rand()"))
				.setMaxResults(ModelParameters.slideShowNbPhotosToRefresh)
				.list();
			
			return list;
		}
	}

	@Override
	public List<Photo> findXBestPhotosFromCategory(int id, int index) {
		List<Category> subCategories = findSubCategories(id);
		List<Integer> categoriesIds = new ArrayList<>();
		
		categoriesIds.add(id);
		
		for (Category category : subCategories) {
			categoriesIds.add(category.getId());
		}
		
		List<Photo> bestPhotos = getCurrentSession().createCriteria(Photo.class)
			.createAlias("categories", "cat")
			.add(Restrictions.in("cat.id", categoriesIds))
			.addOrder(Order.desc("score"))
			.addOrder(Order.asc("fileName"))
			.setFirstResult(index)
			.setMaxResults(ModelParameters.slideShowNbPhotosToRefresh)
			.list();
		
		return bestPhotos;
	}

	@Override
	public boolean hasSubCategories(int categoryId) {
		int nbSubCategories = ((Long) getCurrentSession().createCriteria(Category.class)
			.add(Restrictions.eq("id", categoryId))
			.createCriteria("subCategories")
			.setProjection(Projections.rowCount())
			.uniqueResult()).intValue();
		
		return nbSubCategories == 0 ? false : true;
	}

	@Override
	public List<Category> findParentsCategories() {
		List<Category> parentsCategories = getCurrentSession().createCriteria(Category.class)
			.add(Restrictions.isNull("parentCategory"))
			.addOrder(Order.asc("order"))
			.list();
		
		return parentsCategories;
	}

	@Override
	public List<Category> findAllCategories() {
//		List<Category> categories = getCurrentSession().createQuery("SELECT c FROM Category c ORDER BY c.order").list();
		List<Category> categories = getCurrentSession().createCriteria(Category.class)
			.addOrder(Order.asc("order"))
			.list();
			
		return categories;
	}
	
	public List<Category> findSelectablesCategories() {
		List<Category> selectablesCategories = new ArrayList<>();
		List<Category> categories = findAllCategories();
		
		for (Category category : categories) {
			if (!hasSubCategories(category.getId())) {
				selectablesCategories.add(category);
			}
		}
		
		return selectablesCategories;
	}

	@Override
	public Category findCategoryByName(String name) {
//		Query q = getCurrentSession().createQuery("SELECT c FROM Category c WHERE c.descriptionFr = :name");
//		q.setString("name", name);
//		Category category = (Category) q.uniqueResult();
		
		Category category = (Category) getCurrentSession().createCriteria(Category.class)
			.add(Restrictions.eq("descriptionFr", name))
			.uniqueResult();
		
		return category;
	}
	
}
