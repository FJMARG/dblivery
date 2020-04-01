package ar.edu.unlp.info.bd2.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DBliveryRepository {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public DBliveryRepository() {
		
	}
	

	public <T> T persist(T entity) {
		
		EntityManager em = this.sessionFactory.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(entity);
		
		em.getTransaction().commit();
		em.close();
		
		return entity;
	}
	
	public <T> T update(T entity) {
		EntityManager em = this.sessionFactory.createEntityManager();
		em.getTransaction().begin();
		
		em.merge(entity);
		
		em.getTransaction().commit();
		em.close();
		
		return entity;
	}
//	
//	public <T> void delete(T entity) {
//		this.getEntityManager().remove(entity);
//	}
//
//	public <T> T deleteById(Long id, Class<T> persistentClass) { 
//		T entity = this.getEntityManager().find(persistentClass, id);
//
//		if (entity != null) {
//			this.delete(entity);
//		}	
//		return entity;
//	}
//
//	public <T> boolean exists(Long id, Class<T> persistentClass) {
//		T entity = this.getEntityManager().find(persistentClass, id);
//
//		if (entity != null) {
//			return true;
//		}	
//		return false;
//	}
//	
	public <T> T get(Long id, Class<T> persistentClass) {	
		EntityManager em = this.sessionFactory.createEntityManager();
		em.getTransaction().begin();
		
		T entity = em.find(persistentClass, id);
		
		em.getTransaction().commit();
		em.close();
		
		return entity;
	}
//	
//	
//	public <T> List<T> getAll(String columnOrder, Class<T> persistentClass) {
//		
//		Query query = this.getEntityManager()
//							.createQuery(" FROM " + persistentClass.getSimpleName()
//										+" ORDER BY " + columnOrder);
//		
//		List<T> resultList = (List<T>)query.getResultList();
//		
//		return resultList;
//	}	




}
