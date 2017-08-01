package ch.blutch.battlephoto.model.service;

import java.util.List;

import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Photo;

public interface PhotoService {

	public List<Photo> findXRandomPhotos(List<Photo> actualPhotosList);
	
	public List<Photo> findXBestPhotos(int index, boolean isForTemplate);
	
	public void updatePhotoScore(int photoId);
	
	public List<Category> findCategories(int photoId);
	
	public boolean userCanNote(int userId, int photoId);
	
	public boolean ipAddressCanNote(String ipAddress, int photoId);
	
	public int findNbTotalPhotos();
	
}
