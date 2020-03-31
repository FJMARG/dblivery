package ar.edu.unlp.info.bd2.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DBliveryRepository {
	
	@Autowired
	SessionFactory sessionFactory;
	
	EntityManager entityManager;
	
	public DBliveryRepository() {
		this.entityManager = this.sessionFactory.createEntityManager();
	}
	
	
	public EntityManager getEntityManager() {
		return entityManager;
	}


	public <T> T persist(T entity) {
		this.getEntityManager().persist(entity);
		return entity;
	}
	
	public <T> T update(T entity) {
		this.getEntityManager().merge(entity);
		return entity;
	}
	
	public <T> void delete(T entity) {
		this.getEntityManager().remove(entity);
	}

	public <T> T deleteById(Long id, Class<T> persistentClass) { 
		T entity = this.getEntityManager().find(persistentClass, id);

		if (entity != null) {
			this.delete(entity);
		}	
		return entity;
	}

	public <T> boolean exists(Long id, Class<T> persistentClass) {
		T entity = this.getEntityManager().find(persistentClass, id);

		if (entity != null) {
			return true;
		}	
		return false;
	}
	
	public <T> T get(Long id, Class<T> persistentClass) {	
		T entity = this.getEntityManager().find(persistentClass, id);
		return entity;	
	}
	
	
	public <T> List<T> getAll(String columnOrder, Class<T> persistentClass) {
		
		Query query = this.getEntityManager()
							.createQuery(" FROM " + persistentClass.getSimpleName()
										+" ORDER BY " + columnOrder);
		
		List<T> resultList = (List<T>)query.getResultList();
		
		return resultList;
	}	




}
