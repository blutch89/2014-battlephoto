package ch.blutch.battlephoto.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.impl.CategoryDaoImpl;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.service.CategoryService;

@Service("categoryService")
@Transactional(readOnly = true)
public class CategoryServiceImpl extends GenericServiceImpl<Category> implements CategoryService {
	
	@Autowired
	private CategoryDaoImpl categoryDao;

	@Override
	public Category findCategoryById(int id) {
		return categoryDao.findById(id);
	}
	
	@Override
	public List<Category> findSubCategories(int id) {
		return categoryDao.findSubCategories(id);
	}

	@Override
	public List<Photo> findXRandomPhotosFromCategory(int id, List<Photo> actualPhotosList) {
		return categoryDao.findXRandomPhotosFromCategory(id, actualPhotosList);
	}

	@Override
	public boolean hasSubCategories(int categoryId) {
		return categoryDao.hasSubCategories(categoryId);
	}

	@Override
	public List<Category> findParentsCategories() {
		return categoryDao.findParentsCategories();
	}

	@Override
	public int findNbPhotosInCategoryAndSubCategories(int id) {
		return categoryDao.findNbPhotosInCategoryAndSubCategories(id);
	}

	@Override
	public List<Category> findAllCategories() {
		return categoryDao.findAllCategories();
	}

	@Override
	public List<Photo> findXBestPhotosFromCategory(int id, int index) {
		return categoryDao.findXBestPhotosFromCategory(id, index);
	}
	
	public List<Category> findSelectablesCategories() {
		return categoryDao.findSelectablesCategories();
	}

	@Override
	public Category findCategoryByName(String name) {
		return categoryDao.findCategoryByName(name);
	}
	
	// !!!! ne pas oublier les transactions à mettre dans les services uniquement
	
}
