package ch.blutch.battlephoto.model.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao <T> {

	public T findById(Integer id);
	
	public List<T> findAll();
	
	public Serializable save(T entity);
	
	public void saveOrUpdate(T entity);
	
	public void delete(T entity);
	
	public void merge(T entity);
	
}
