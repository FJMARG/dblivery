package ar.edu.unlp.info.bd2.repositories;

import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.unlp.info.bd2.model.Order;
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
	
	
	public List<Order> getAllOrdersMadeByUser(User user) {
		EntityManager em = this.getEntityManager();
		Query query = em.createQuery("FROM Order o WHERE o.client.id = " + user.getId() );
		
		ArrayList<Order> list = new ArrayList<Order>();
		
		em.getTransaction().begin();
		
		list = (ArrayList<Order>) query.getResultList();
		
		em.getTransaction().commit();
		
		return list;
	}
	
	
	public List<User> getUsersSpendingMoreThan(Float amount) {
		EntityManager em = this.getEntityManager();
		
		Query query = em.createQuery("SELECT u FROM User AS u "
									+"JOIN u.orders AS o "
									+"JOIN o.status AS os "
									+"WHERE os.id = 2 "
									+"GROUP BY u.id, o.id "
									+"HAVING SUM(o.amount) > " + amount);

		ArrayList<User> list = new ArrayList<User>();
		em.getTransaction().begin();
		
		list = (ArrayList<User>) query.getResultList();
		
		em.getTransaction().commit();
		
		return list;
	}
	
	
	public ArrayList<Product> getTop10MoreExpensiveProducts(){
		EntityManager em = this.getEntityManager();
		ArrayList<Product> list = new ArrayList<Product>();
		
		Query query = em.createQuery("SELECT prod FROM Product as prod "
										+ "JOIN prod.prices as pri "
										+ "ORDER BY pri.price DESC").setFirstResult(0).setMaxResults(9);
		
		em.getTransaction().begin();
		
		list = (ArrayList<Product>) query.getResultList();
		
		em.getTransaction().commit();	
		
		return list;
	}
	
	public List<Product> getProductsOnePrice(){
		EntityManager em = this.getEntityManager();
		List<Product> list = new ArrayList<Product>();
		
		Query query = em.createQuery("SELECT prod FROM Product as prod "
										+ "JOIN prod.prices as pr "
										+ "GROUP BY prod "
										+ "HAVING COUNT(*) = 1");
		
		em.getTransaction().begin();
		
		list = (ArrayList<Product>) query.getResultList();
		
		em.getTransaction().commit();	
		
		return list;
		
	}
	
	public List<Product> getSoldProductsOn( Date day ){
		EntityManager em = this.getEntityManager();
		List<Product> list = new ArrayList<Product>();
		java.sql.Date dbdate = new java.sql.Date(day.getYear(), day.getMonth(), day.getDate());
		
		System.out.println(dbdate);
		Query query = em.createQuery("SELECT prod FROM Order as o "
										+ "JOIN o.products as po "
										+ "JOIN po.product as prod "
										+ "WHERE o.date = '" + dbdate + "'");
		
		em.getTransaction().begin();
		
		list = (ArrayList<Product>) query.getResultList();
		
		em.getTransaction().commit();	
		return list;
		
	}
	
	public List<Product> getProductsNotSold(){
		EntityManager em = this.getEntityManager();
		List<Product> list = new ArrayList<Product>();
		
		String q = "SELECT prod FROM Product as prod "
				+ "WHERE prod NOT IN "
				+ "(SELECT DISTINCT p FROM Order o "
				+ "JOIN o.products po "
				+ "JOIN po.product p)";
		
		Query query = em.createQuery(q);
		
		em.getTransaction().begin();
		
		list = (ArrayList<Product>) query.getResultList();
		
		em.getTransaction().commit();	
		return list;
		
	}
	
//	la queri hql corresponde a la sql con quantity sin chequear estado
	public Product getBestSellingProduct() {
		EntityManager em = this.getEntityManager();
		ArrayList<Product> p = new ArrayList<Product>();
		Query query = em.createQuery("SELECT p from ProductOrder as po "
									+ "JOIN po.product as p "
									+ "GROUP BY p "
									+ "ORDER BY COUNT(po.quantity) DESC");
		
		em.getTransaction().begin();
		
		p = (ArrayList<Product>) query.getResultList();
		
		em.getTransaction().commit();	
		
		return p.get(0);
	}

	public List<Order> getOrdersOrderedByQuantityOfProducts(String day){
		
		EntityManager em = this.getEntityManager();
		
		String q = "SELECT o FROM Order o JOIN o.products p WHERE o.date = '"+day+"' GROUP BY o ORDER BY sum(p.quantity) DESC";
		
		Query query = em.createQuery(q);
		
		em.getTransaction().begin();
		
		List<Order> list = query.getResultList();
		
		em.getTransaction().commit();	
		return list;
	}
	
	public List<Object[]> getProductsWithPriceAt(String day) {
		
		EntityManager em = this.getEntityManager();
		String q = "SELECT p,  pr.price FROM Product p JOIN p.prices pr WHERE '"+day+"' BETWEEN pr.startDate AND pr.endDate";
		Query query = em.createQuery(q);
		em.getTransaction().begin();
		List<Object[]> list = query.getResultList();
		em.getTransaction().commit();	
		return list;
	
	}
	
}
