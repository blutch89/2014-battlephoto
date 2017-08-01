package ch.blutch.battlephoto.model.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.blutch.battlephoto.model.dao.GenericDao;

@Repository("genericDao")
@Transactional(readOnly = true)
public class GenericDaoImpl<T> implements GenericDao<T> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> clazz;
	
	// A ne pas utiliser. Utile pour Spring qui a besoin d'un constructeur sans paramètres
	@Deprecated
	public GenericDaoImpl() {
		
	}
	
	public GenericDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
    }
	
	@Override
	public T findById(Integer id) {
		return (T) getCurrentSession().get(clazz, id);
	}

	@Override
	public List<T> findAll() {
		List<T> instances = getCurrentSession().createCriteria(clazz).list();
		
		return instances;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Serializable save(T entity) {
		return getCurrentSession().save(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdate(T entity) {
		getCurrentSession().saveOrUpdate(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(T entity) {
		getCurrentSession().delete(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void merge(T entity) {
		getCurrentSession().merge(entity);
	}
	
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

}
