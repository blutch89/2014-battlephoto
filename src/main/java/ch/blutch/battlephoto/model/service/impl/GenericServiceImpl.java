package ch.blutch.battlephoto.model.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.impl.GenericDaoImpl;
import ch.blutch.battlephoto.model.service.GenericService;

@Service("genericService")
@Transactional(readOnly = true)
public class GenericServiceImpl<T> implements GenericService<T> {

	@Autowired
	private GenericDaoImpl<T> genericDao;
	
	private Class<T> clazz;
	
	// A ne pas utiliser. Utile pour Spring qui a besoin d'un constructeur sans paramètres
	@Deprecated
	public GenericServiceImpl() {
		
	}
	
	public GenericServiceImpl(Class<T> clazz) {
        this.clazz = clazz;
    }
	
	@Override
	public T findById(Integer id) {
		return genericDao.findById(id);
	}

	@Override
	public List<T> findAll() {
		return genericDao.findAll();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Serializable save(T entity) {
		return genericDao.save(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdate(T entity) {
		genericDao.saveOrUpdate(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(T entity) {
		genericDao.delete(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void merge(T entity) {
		genericDao.merge(entity);
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
		genericDao.setClazz(clazz);
	}

}
