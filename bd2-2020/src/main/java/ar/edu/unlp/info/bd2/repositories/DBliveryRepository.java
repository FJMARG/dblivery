package ar.edu.unlp.info.bd2.repositories;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.OrderStatus;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.Supplier;
import ar.edu.unlp.info.bd2.model.User;

public class DBliveryRepository {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public DBliveryRepository() {}
	
	public <T> T persist(T entity) {
		Session s = this.sessionFactory.getCurrentSession();
		s.persist(entity);
		return entity;
	}
	
	public <T> T update(T entity) {
		Session s = this.sessionFactory.getCurrentSession();
		s.merge(entity);
		return entity;
	}

	public <T> T get(Long id, Class<T> persistentClass) {	
		Session s = this.sessionFactory.getCurrentSession();
		T entity = s.find(persistentClass, id);	
		return entity;
	}
	
//	##################################################################################
// 						METODOS PRIVADOS DE CADA CLASE
//	##################################################################################
	
//	Supplier
	public List<Supplier> getTopNSuppliersInSentOrders( int n ){
		Session s = this.sessionFactory.getCurrentSession();
		ArrayList<Supplier> list = new ArrayList<Supplier>();
		
		Query query = s.createQuery("SELECT sup FROM Order as o "
						+ "JOIN o.products as po "
						+ "JOIN po.product as prod "
						+ "JOIN prod.supplier as sup "
						+ "GROUP BY sup "
						+ "ORDER BY COUNT(*) DESC").setFirstResult(0).setMaxResults(n);
		
		list = (ArrayList<Supplier>) query.getResultList();
		
		return list;	
	}
	
//	Order Status
	public OrderStatus getStatusByName(String status) {	
		Session s = this.sessionFactory.getCurrentSession();
		Query query = s.createQuery("FROM OrderStatus WHERE status = '" + status + "'");
		OrderStatus result;
		if (query.getResultList().isEmpty()) // FIX
			result = null;
		else
			result = (OrderStatus) query.getSingleResult();
		return result;
	}
	
//	User
	public User getUserByUsername(String username) {	
		Session s = this.sessionFactory.getCurrentSession();
		Query query = s.createQuery("FROM User WHERE username = '" + username + "'");
		User result;
		try{
			result = (User) query.getSingleResult();
		}catch( Exception e) {
			result = null;
		}
		return result;
	}
	
	public User getUserByEmail(String email) {	
		Session s = this.sessionFactory.getCurrentSession();
		Query query = s.createQuery("FROM User WHERE email = '" + email + "'");
		User result = (User) query.getSingleResult();		
		return result;
	}
	
	public List<User> get5LessDeliveryUsers(){
		Session s = this.sessionFactory.getCurrentSession();
		
		Query query = s.createQuery("SELECT u FROM Order as o "
									+ "JOIN o.currentStatus as s "
									+ "JOIN o.deliveryUser as u "
									+ "WHERE (s.id = 2) or (s.id = 4) "
									+ "GROUP BY u "
									+ "ORDER BY count(o) ASC").setFirstResult(0).setMaxResults(5);
		
		ArrayList<User> list = new ArrayList<User>();
		list = (ArrayList<User>) query.getResultList();
		return list;
		
	}
	
//	Product
	@SuppressWarnings("unchecked")
	public List<Product> getProductsByName(String name) {
		Session s = this.sessionFactory.getCurrentSession();
		Query query = s.createQuery("FROM Product WHERE name LIKE '%" + name + "%'");
		ArrayList<Product> list = new ArrayList<Product>();
		list = (ArrayList<Product>) query.getResultList();
		return list;
	}
	
	public Product getProductByName(String name) {
		Session s = this.sessionFactory.getCurrentSession();
		Query query = s.createQuery("FROM Product WHERE name = '" + name + "'");
		Product product;
		try {
			product = (Product) query.getSingleResult();
		}catch( Exception e ) {
			product = null;
		}
		return product;
	}
	
	@SuppressWarnings("unchecked")
	public List<Order> getAllOrdersMadeByUser(User user) {
		Session s = this.sessionFactory.getCurrentSession();
		Query query = s.createQuery("FROM Order o WHERE o.client.id = " + user.getId() );
		ArrayList<Order> list = new ArrayList<Order>();
		list = (ArrayList<Order>) query.getResultList();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUsersSpendingMoreThan(Float amount) {
		System.out.println("AMOUNT");
		System.out.println(amount);
		Session s = this.sessionFactory.getCurrentSession();	
		Query query = s.createQuery("SELECT u FROM User AS u "
									+"JOIN u.orders AS o "
									+"WHERE o.amount > " + amount);
		ArrayList<User> list = new ArrayList<User>();
		list = (ArrayList<User>) query.getResultList();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Product> getTop10MoreExpensiveProducts(){
		Session s = this.sessionFactory.getCurrentSession();
		ArrayList<Product> list = new ArrayList<Product>();	
		Query query = s.createQuery("SELECT prod FROM Product as prod "
										+ "JOIN prod.prices as pri "
										+ "ORDER BY pri.price DESC").setFirstResult(0).setMaxResults(9);
		list = (ArrayList<Product>) query.getResultList();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductsOnePrice(){
		Session s = this.sessionFactory.getCurrentSession();
		List<Product> list = new ArrayList<Product>();
		Query query = s.createQuery("SELECT prod FROM Product as prod "
										+ "JOIN prod.prices as pr "
										+ "GROUP BY prod "
										+ "HAVING COUNT(*) = 1");
		list = (ArrayList<Product>) query.getResultList();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getSoldProductsOn( Date day ){
		Session s = this.sessionFactory.getCurrentSession();
		List<Product> list = new ArrayList<Product>();
		@SuppressWarnings("deprecation")
		java.sql.Date dbdate = new java.sql.Date(day.getYear(), day.getMonth(), day.getDate());
		System.out.println(dbdate);
		Query query = s.createQuery("SELECT prod FROM Order as o "
										+ "JOIN o.products as po "
										+ "JOIN po.product as prod "
										+ "WHERE o.date = '" + dbdate + "'");
		list = (ArrayList<Product>) query.getResultList();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductsNotSold(){
		Session s = this.sessionFactory.getCurrentSession();
		List<Product> list = new ArrayList<Product>();
		String q = "SELECT prod FROM Product as prod "
				+ "WHERE prod NOT IN "
				+ "(SELECT DISTINCT p FROM Order o "
				+ "JOIN o.products po "
				+ "JOIN po.product p)";
		Query query = s.createQuery(q);
		list = (ArrayList<Product>) query.getResultList();	
		return list;
	}
	
	//	la query hql corresponde a la sql con quantity sin chequear estado
	@SuppressWarnings("unchecked")
	public Product getBestSellingProduct() {
		Session s = this.sessionFactory.getCurrentSession();
		ArrayList<Product> p = new ArrayList<Product>();
		Query query = s.createQuery("SELECT p from ProductOrder as po "
									+ "JOIN po.product as p "
									+ "GROUP BY p "
									+ "ORDER BY COUNT(po.quantity) DESC");
		p = (ArrayList<Product>) query.getResultList();
		return p.get(0);
	}

	public List<Order> getOrdersOrderedByQuantityOfProducts(String day){
		Session s = this.sessionFactory.getCurrentSession();
		String q = "SELECT o FROM Order o JOIN o.products p WHERE o.date = '"+day+"' GROUP BY o ORDER BY sum(p.quantity) DESC";
		Query query = s.createQuery(q);
		@SuppressWarnings("unchecked")
		List<Order> list = query.getResultList();
		return list;
	}
	
	public List<Object[]> getProductsWithPriceAt(String day) {
		Session s = this.sessionFactory.getCurrentSession();
		String q = "SELECT p,  pr.price FROM Product p JOIN p.prices pr WHERE '"+day+"' BETWEEN pr.startDate AND pr.endDate";
		Query query = s.createQuery(q);
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();	
		return list;
	}
	
//	Orders
	
	public List<Order> getPendingOrders(){
		Session s = this.sessionFactory.getCurrentSession();
		ArrayList<Order> orders = new ArrayList<Order>();
		
		Query query = s.createQuery("SELECT o from Order as o "
									+ "JOIN o.currentStatus as s "
									+ "WHERE s.id = 1");
		
		orders = (ArrayList<Order>) query.getResultList();
		return orders;
	}
	
	public List<Order> getSentOrders(){
		Session s = this.sessionFactory.getCurrentSession();
		ArrayList<Order> orders = new ArrayList<Order>();
		
		Query query = s.createQuery("SELECT o from Order as o "
									+ "JOIN o.currentStatus as s "
									+ "WHERE s.id = 2");
		
		orders = (ArrayList<Order>) query.getResultList();
		return orders;
	}
	
	public List<Order> getDeliveredOrdersForUser(String username) {
		Session s = this.sessionFactory.getCurrentSession();
		ArrayList<Order> orders = new ArrayList<Order>();
		
		Query query = s.createQuery("SELECT o from Order as o "
									+ "JOIN o.currentStatus as s "
									+ "JOIN o.client as u "
									+ "WHERE s.id = 4 AND u.username LIKE '" + username + "'");
		
		orders = (ArrayList<Order>) query.getResultList();
		return orders;
	}
}
