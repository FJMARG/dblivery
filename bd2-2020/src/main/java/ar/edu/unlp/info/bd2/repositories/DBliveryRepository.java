package ar.edu.unlp.info.bd2.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.unlp.info.bd2.model.OrderStatus;
import ar.edu.unlp.info.bd2.model.Pending;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.User;

public class DBliveryRepository {
	
	@Autowired
	SessionFactory sessionFactory;
	
	private EntityManager entityManager;
	
	public DBliveryRepository() {}

	public EntityManager getEntityManager() {
		if (this.entityManager == null)
			this.entityManager = this.sessionFactory.createEntityManager();
		return this.entityManager;
	}
	
	public <T> T persist(T entity) {
		
		EntityManager em = this.getEntityManager();
		em.getTransaction().begin();
		
		em.persist(entity);
		
		em.getTransaction().commit();
		
		return entity;
	}
	
	public <T> T update(T entity) {
		EntityManager em = this.getEntityManager();
		em.getTransaction().begin();
		
		em.merge(entity);
		
		em.getTransaction().commit();
		
		return entity;
	}

	public <T> T get(Long id, Class<T> persistentClass) {	
		EntityManager em = this.getEntityManager();
		em.getTransaction().begin();
		
		T entity = em.find(persistentClass, id);
		
		em.getTransaction().commit();
		
		return entity;
	}
	
//	##################################################################################
// 						METODOS PRIVADOS DE CADA CLASE
//	##################################################################################
	
//	Order Status
	public OrderStatus getStatusByName(String status) {	
		EntityManager em = this.getEntityManager();
		Query query = em.createQuery("FROM OrderStatus WHERE status = '" + status + "'");
		
		OrderStatus result;
		
		em.getTransaction().begin();
		if (query.getResultList().isEmpty()) // FIX
			result = null;
		else
			result = (OrderStatus) query.getSingleResult();
		
		em.getTransaction().commit();
		
		return result;
	}
	
//	User
	public User getUserByUsername(String username) {	
		EntityManager em = this.getEntityManager();
		Query query = em.createQuery("FROM User WHERE username = '" + username + "'");
		User result;
		
		em.getTransaction().begin();
		try{
			result = (User) query.getSingleResult();
		}catch( Exception e) {
			result = null;
		}
				
		em.getTransaction().commit();
		
		return result;
	}
	
	public User getUserByEmail(String email) {	
		EntityManager em = this.getEntityManager();
		Query query = em.createQuery("FROM User WHERE email = '" + email + "'");
		
		em.getTransaction().begin();
		
		User result = (User) query.getSingleResult();
	
		em.getTransaction().commit();
		
		return result;
	}
	
//	Product
	public List<Product> getProductsByName(String name) {
		
		EntityManager em = this.getEntityManager();
		Query query = em.createQuery("FROM Product WHERE name LIKE '%" + name + "%'");
		
		ArrayList<Product> list = new ArrayList<Product>();
		
		em.getTransaction().begin();
		
		list = (ArrayList<Product>) query.getResultList();
		
		em.getTransaction().commit();	
		
		return list;
		
	}
	
	public Product getProductByName(String name) {
		
		EntityManager em = this.getEntityManager();
		Query query = em.createQuery("FROM Product WHERE name = '" + name + "'");
		
		Product product;
		
		em.getTransaction().begin();	 
		try {
			product = (Product) query.getSingleResult();
		}catch( Exception e ) {
			product = null;
		}
		em.getTransaction().commit();
		
		return product;
		
	}
	
	public ArrayList<Product> getTop10MoreExpensiveProducts(){
		EntityManager em = this.getEntityManager();
		ArrayList<Product> list = new ArrayList<Product>();
		
		Query query = em.createQuery("SELECT prod FROM Product as prod "
										+ "JOIN prod.prices as pri "
										+ "WHERE pri.endDate is null "
										+ "ORDER BY pri.price DESC").setFirstResult(0).setMaxResults(10);
		
		em.getTransaction().begin();
		
		list = (ArrayList<Product>) query.getResultList();
		
		em.getTransaction().commit();	
		
		return list;
	}

}
