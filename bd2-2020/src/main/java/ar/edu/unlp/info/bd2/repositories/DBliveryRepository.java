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

	public <T> T get(Long id, Class<T> persistentClass) {	
		EntityManager em = this.sessionFactory.createEntityManager();
		em.getTransaction().begin();
		
		T entity = em.find(persistentClass, id);
		
		em.getTransaction().commit();
		em.close();
		
		return entity;
	}

}
