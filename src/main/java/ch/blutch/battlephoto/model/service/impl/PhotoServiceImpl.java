package ch.blutch.battlephoto.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.impl.PhotoDaoImpl;
import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Photo;
import ch.blutch.battlephoto.model.service.PhotoService;

@Service("photoService")
@Transactional(readOnly = true)
public class PhotoServiceImpl extends GenericServiceImpl<Photo> implements PhotoService {

	@Autowired
	private PhotoDaoImpl photoDao;

	@Override
	public List<Photo> findXRandomPhotos(List<Photo> actualPhotosList) {
		return photoDao.findXRandomPhotos(actualPhotosList);
	}

	@Override
	public List<Photo> findXBestPhotos(int index, boolean isForTemplate) {
		return photoDao.findXBestPhotos(index, isForTemplate);
	}

	@Override
	public List<Category> findCategories(int photoId) {
		return photoDao.findCategories(photoId);
	}

	@Override
	public boolean userCanNote(int userId, int photoId) {
		return photoDao.userCanNote(userId, photoId);
	}

	@Override
	public boolean ipAddressCanNote(String ipAddress, int photoId) {
		return photoDao.ipAddressCanNote(ipAddress, photoId);
	}

	@Override
	public int findNbTotalPhotos() {
		return photoDao.findNbTotalPhotos();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updatePhotoScore(int photoId) {
		photoDao.updatePhotoScore(photoId);
	}
	
	// !!!! ne pas oublier les transactions à mettre dans les services uniquement
	
}
