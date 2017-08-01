package ch.blutch.battlephoto.model.dao;

import java.util.List;

import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Photo;


public interface CategoryDao {
	
	//TODO : Faire attention à prendre les descriptions dans toutes les langues
	public Category findCategoryByName(String name);
	
	public List<Category> findAllCategories();

	public List<Category> findSubCategories(int id);
	
	public int findNbPhotosInCategoryAndSubCategories(int id);
	
	public List<Photo> findXRandomPhotosFromCategory(int id, List<Photo> actualPhotosList);
	
	public List<Photo> findXBestPhotosFromCategory(int id, int index);
	
	public boolean hasSubCategories(int categoryId);
	
	public List<Category> findParentsCategories();
	
	public List<Category> findSelectablesCategories();
	
}
