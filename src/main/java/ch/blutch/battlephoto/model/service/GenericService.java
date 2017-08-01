package ch.blutch.battlephoto.model.service;

import java.io.Serializable;
import java.util.List;

public interface GenericService <T> {

	public T findById(Integer id);
	
	public List<T> findAll();
	
	public Serializable save(T entity);
	
	public void saveOrUpdate(T entity);
	
	public void delete(T entity);
	
	public void merge(T entity);
	
}
