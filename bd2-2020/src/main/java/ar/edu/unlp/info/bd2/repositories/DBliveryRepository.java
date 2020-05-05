package ar.edu.unlp.info.bd2.repositories;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.Status;
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
						+ "JOIN o.status as os "
						+ "WHERE os.status.class = Sent "
						+ "GROUP BY sup "
						+ "ORDER BY COUNT(*) DESC").setFirstResult(0).setMaxResults(n);
		
		list = (ArrayList<Supplier>) query.getResultList();
		
		return list;	
	}
	
//	Order Status
	public Status getStatusByName(String status) {	
		Session s = this.sessionFactory.getCurrentSession();
		Query query = s.createQuery("FROM Status WHERE status = '" + status + "'");
		Status result;
		if (query.getResultList().isEmpty()) // FIX
			result = null;
		else
			result = (Status) query.getSingleResult();
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
									+ "JOIN o.currentStatus as os "
									+ "JOIN o.deliveryUser as u "
									+ "WHERE (os.status.class = Sent) or (os.status.class = Delivered) "
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
	
	public List<Product> getProductIncreaseMoreThan100(){
		Session s = this.sessionFactory.getCurrentSession();
		
		Query query = s.createQuery("SELECT DISTINCT prod "
									+ "FROM Product as prod "
									+ "JOIN prod.prices as pric "
									+ "WHERE pric.price * 2 <= "
									+ "(SELECT MAX(pri.price) "
									+ "FROM Product as p "
									+ "JOIN p.prices as pri "
									+ "GROUP BY p "
									+ "HAVING p = prod)");
		
		ArrayList<Product> list = new ArrayList<Product>();
		list = (ArrayList<Product>) query.getResultList();
		return list;
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
										+ "JOIN prod.currentPrice as pri "
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

	public List<Order> getOrderWithMoreQuantityOfProducts(String day){
		Session s = this.sessionFactory.getCurrentSession();
		
		System.out.println("FECHA");
		System.out.println(day);
		String q = "SELECT o FROM Order o JOIN o.products p WHERE o.date = '"+day+"' GROUP BY o ORDER BY sum(p.quantity) DESC";
		Query query = s.createQuery(q).setFirstResult(0).setMaxResults(1);
		//String qSum = "(SELECT sum(p.quantity) FROM Order o JOIN o.products p WHERE o.date = '"+day+"')";
		//String q = "SELECT o FROM Order o JOIN o.products p WHERE o.date = '"+day+"' AND sum(p.quantity) =  max("+qSum+") GROUP BY o";
		//Query query = s.createQuery(q);
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
									+"JOIN o.currentStatus as os "
									+"WHERE os.status.class = Pending");
		
		orders = (ArrayList<Order>) query.getResultList();
		return orders;
	}
	
	public List<Order> getSentOrders(){
		Session s = this.sessionFactory.getCurrentSession();
		ArrayList<Order> orders = new ArrayList<Order>();
		
		Query query = s.createQuery("SELECT o from Order as o "
									+ "JOIN o.currentStatus as os "
									+ "WHERE os.status.class = Sent");
		
		orders = (ArrayList<Order>) query.getResultList();
		return orders;
	}
	
	public List<Order> getDeliveredOrdersForUser(String username) {
		Session s = this.sessionFactory.getCurrentSession();
		ArrayList<Order> orders = new ArrayList<Order>();
		
		Query query = s.createQuery("SELECT o from Order as o "
									+ "JOIN o.currentStatus as os "
									+ "JOIN o.client as u "
									+ "WHERE os.status.class = Delivered AND u.username LIKE '" + username + "'");
		
		orders = (ArrayList<Order>) query.getResultList();
		return orders;
	}
	
//	Supplier
	public Supplier getSupplierLessExpensiveProduct() {
		Session s = this.sessionFactory.getCurrentSession();
		
		Query query = s.createQuery("SELECT s from Product as p "
									+ "JOIN p.supplier as s "
									+ "JOIN p.prices as pri "
									+ "WHERE pri.price = (SELECT MIN(pr.price) FROM Price as pr)");
		
		Supplier supplier = (Supplier) query.getSingleResult();
		return supplier;
	}
	
	public List<Order> getOrdersCompleteMorethanOneDay() {
		Session s = this.sessionFactory.getCurrentSession();
		
		Query query = s.createQuery("SELECT s from Order o "
									+ "JOIN o.status os "
									+ "JOIN os.status s "
									+ "WHERE s.class = Pending AND (o.currentStatus.status.class = Delivered) AND (o.currentStatus.date - os.date) >= 1");
		
		return query.getResultList();
	}
	
	public List<Order> getDeliveredOrdersSameDay() {
		Session s = this.sessionFactory.getCurrentSession();
		
		Query query = s.createQuery("SELECT s from Order o "
									+ "JOIN o.status os "
									+ "JOIN os.status s "
									+ "WHERE s.class = Pending AND (o.currentStatus.status.class = Delivered) AND (o.currentStatus.date - os.date) = 0");
		
		return query.getResultList();
	}
	
	public List<Order> getDeliveredOrdersInPeriod(String startDate, String endDate) {
		Session s = this.sessionFactory.getCurrentSession();
		
		Query query = s.createQuery("SELECT o from Order o "
									+ "WHERE (o.currentStatus.status.class = Delivered) AND (o.currentStatus.date BETWEEN '"+startDate+"' AND '"+endDate+"')");
		
		return query.getResultList();
	}
	
	public List<Order> getCancelledOrdersInPeriod(String startDate, String endDate) {
		Session s = this.sessionFactory.getCurrentSession();
		
		Query query = s.createQuery("SELECT o from Order o "
									+ "WHERE (o.currentStatus.status.class = Cancelled) AND (o.currentStatus.date BETWEEN '"+startDate+"' AND '"+endDate+"')");
		
		return query.getResultList();
	}
	
	public List<User> getTop6UsersMoreOrders() {
		Session s = this.sessionFactory.getCurrentSession();
		
		Query query = s.createQuery("SELECT u FROM User u "
									+ "JOIN u.orders o "
									+ "GROUP BY u "
									+ "ORDER BY count(o.id) DESC").setMaxResults(6);
		
		return query.getResultList();
	}
	
	
	public List<Order> getSentMoreOneHour() {
		Session s = this.sessionFactory.getCurrentSession();
		
		Query query = s.createQuery("SELECT o FROM Order AS o "
									+"JOIN o.status AS os_sent "
									+"WHERE os_sent.status.class = Sent "
									+"AND DATEDIFF(os_sent.date, o.date) >= 1");
		
		return query.getResultList();
	}

	
	public List<Supplier> getSuppliersDoNotSellOn(Date day) {
		Session s = this.sessionFactory.getCurrentSession();
		
		@SuppressWarnings("deprecation")
		java.sql.Date dbdate = new java.sql.Date(day.getYear(), day.getMonth(), day.getDate());
		
		Query query = s.createQuery("FROM Supplier "
									+"WHERE id NOT IN "
									+"(SELECT s.id FROM Order AS o "
									+"JOIN o.products AS po "
									+"JOIN po.product AS p "
									+"JOIN p.supplier AS s "
									+"WHERE o.date = '"+ dbdate +"' )");
		
		return query.getResultList();
	}
	
	
	
}
