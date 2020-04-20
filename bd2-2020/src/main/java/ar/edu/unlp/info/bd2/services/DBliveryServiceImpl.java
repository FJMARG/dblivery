package ar.edu.unlp.info.bd2.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.unlp.info.bd2.model.*;
import ar.edu.unlp.info.bd2.repositories.DBliveryException;
import ar.edu.unlp.info.bd2.repositories.DBliveryRepository;

public class DBliveryServiceImpl implements DBliveryService {

	DBliveryRepository repository;
	
	public DBliveryServiceImpl(DBliveryRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	@Transactional
	public Product createProduct(String name, Float price, Float weight, Supplier supplier) {
		Product product = repository.getProductByName(name);
		if( product == null) {
			Product newProduct = new Product(name, price, supplier, weight);			
			return repository.persist(newProduct);
		}
		else {
			return product;
		}
	}
	
	@Override
	@Transactional
	public Product createProduct(String name, Float price, Float weight, Supplier supplier, Date sdf) { // Problema entre test 1 y 2
		//Product product = repository.getProductByName(name);
		//if( product == null) {
			Product newProduct = new Product(name, price, supplier, weight, sdf);
			return repository.persist(newProduct);
		//}
		//else {
		//	return product;
		//}
	}

	@Override
	@Transactional
	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
		Supplier newSupplier = new Supplier(name, cuil, address, coordX, coordY);	
		return repository.persist(newSupplier);
	}
	
	@Override
	@Transactional
	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
		User user = repository.getUserByUsername(username);
		if (user != null) {
			//ya existe un usuario con ese username, lo devuelvo
			return user;
		}
		User newUser = new User(email, password, username, name, dateOfBirth);
		return repository.persist(newUser);
	}

	@Override
	@Transactional
	public Product updateProductPrice(Long id, Float price, Date startDate) throws DBliveryException {
		Product product = repository.get(id, Product.class);
		if (product == null) {
			throw new DBliveryException("El producto solicitado no existe");
		}		
		product.updatePrice(price, startDate);
		return repository.update(product);
	}

	@Override
	@Transactional
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		Order newOrder = new Order(client, coordX, coordY, address, dateOfOrder);
		//busco el estado pending
		Status pending = this.createStatusIfNotExist(new Pending());
		OrderStatus newOrderStatus = new OrderStatus(dateOfOrder, pending);
		newOrderStatus = repository.persist(newOrderStatus);
		newOrder.setStatus(newOrderStatus);
		return repository.persist(newOrder);
	}
	
	@Transactional
	public Status createStatusIfNotExist(Status status) {
		Status o = repository.getStatusByName(status.getStatus());
	
		if (o == null) 
			o = repository.persist(status);
		return o;
	}
	
	@Override
	@Transactional
	public Order addProduct(Long order, Long quantity, Product product) throws DBliveryException {
		Product p = repository.get(product.getId(), Product.class);
		Order updatedOrder = repository.get(order, Order.class);
		if (updatedOrder == null) {
			throw new DBliveryException("El pedido solicitado no existe");
		}
		ProductOrder po = new ProductOrder(quantity, p, updatedOrder);
		po = repository.persist(po);
		updatedOrder.addProductOrder(po);
		return repository.update(updatedOrder);
	}
	
	@Override
	@Transactional
	public Optional<User> getUserById(Long id) {
		User u = repository.get(id, User.class);
		return Optional.of(u);
	}

	@Override
	@Transactional
	public Optional<User> getUserByEmail(String email) {
		User u = repository.getUserByEmail(email);
		return Optional.of(u);
	}

	@Override
	@Transactional
	public Optional<User> getUserByUsername(String username) {
		User u = repository.getUserByUsername(username);
		return Optional.of(u);
	}

	@Override
	@Transactional
	public Optional<Product> getProductById(Long id) {
		Product product = repository.get(id, Product.class);
		return Optional.of(product);
	}

	@Override
	@Transactional
	public Optional<Order> getOrderById(Long id) { // id llega en null desde el test.
		Order order = repository.get(id, Order.class);
		return Optional.of(order);
	}
	
	@Override
	@Transactional
	public OrderStatus getActualStatus(Long order) {
		Optional<Order> orderDB = this.getOrderById(order);
		if(orderDB == null) 
			return null;	
		return orderDB.get().getActualStatus();
	}

	@Override
	@Transactional
	public List<Product> getProductByName(String name) {
		List<Product> productList = repository.getProductsByName(name);
		return productList;
	}
	
	@Override
	@Transactional
	public boolean canCancel(Long order) throws DBliveryException {
		Optional<Order> orderDB = this.getOrderById(order);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		return orderDB.get().canCancel();
	}

	@Override
	@Transactional
	public boolean canFinish(Long id) throws DBliveryException {
		Optional<Order> orderDB = this.getOrderById(id);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		return orderDB.get().canFinish();
	}

	@Override
	@Transactional
	public boolean canDeliver(Long order) throws DBliveryException {
		Optional<Order> orderDB = this.getOrderById(order);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		return orderDB.get().canDeliver();
	}
	
	@Override
	@Transactional
	public Order deliverOrder(Long order, User deliveryUser) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		User userDB   = repository.get(deliveryUser.getId(), User.class);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		if(userDB == null)
			throw new DBliveryException("El usuario asignado no existe");
		if( !orderDB.canDeliver() )
			throw new DBliveryException("The order can't be delivered");
		Status sent = this.createStatusIfNotExist(new Sent());
		OrderStatus newOrderStatus = new OrderStatus(sent);
		newOrderStatus = repository.persist(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		orderDB.setDeliveryUser(userDB);
		return repository.update(orderDB);
	}
	
	@Override
	@Transactional
	public Order deliverOrder(Long order, User deliveryUser, Date sdf) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		User userDB   = repository.get(deliveryUser.getId(), User.class);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		if(userDB == null)
			throw new DBliveryException("El usuario asignado no existe");
		if( !orderDB.canDeliver() )
			throw new DBliveryException("The order can't be delivered");
		Status sent = this.createStatusIfNotExist(new Sent());
		OrderStatus newOrderStatus = new OrderStatus(sdf, sent);
		newOrderStatus = repository.persist(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		orderDB.setDeliveryUser(userDB);
		return repository.update(orderDB);
	}

	@Override
	@Transactional
	public Order cancelOrder(Long order) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		if( !orderDB.canCancel() )
			throw new DBliveryException("The order can't be cancelled");
		Status cancelled = this.createStatusIfNotExist(new Cancelled());
		OrderStatus newOrderStatus = new OrderStatus(cancelled);
		newOrderStatus = repository.persist(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		return repository.update(orderDB);
	}
	
	@Override
	@Transactional
	public Order cancelOrder(Long order, Date sdf) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		if( !orderDB.canCancel() )
			throw new DBliveryException("The order can't be cancelled");
		Status cancelled = this.createStatusIfNotExist(new Cancelled());
		OrderStatus newOrderStatus = new OrderStatus(sdf, cancelled);
		newOrderStatus = repository.persist(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		return repository.update(orderDB);
	}

	@Override
	@Transactional
	public Order finishOrder(Long order) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		if( !orderDB.canFinish() )
			throw new DBliveryException("The order can't be finished");
		Status delivered = this.createStatusIfNotExist(new Delivered());
		OrderStatus newOrderStatus = new OrderStatus(delivered);
		newOrderStatus = repository.persist(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		return repository.update(orderDB);
	}
	
	@Override
	@Transactional
	public Order finishOrder(Long order, Date sdf) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		if( !orderDB.canFinish() )
			throw new DBliveryException("The order can't be finished");
		Status delivered = this.createStatusIfNotExist(new Delivered());
		OrderStatus newOrderStatus = new OrderStatus(sdf, delivered);
		newOrderStatus = repository.persist(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		return repository.update(orderDB);
	}

	@Override
	@Transactional
	public List<Order> getAllOrdersMadeByUser(String username) {	
		Optional<User> userDB = this.getUserByUsername(username);
		if( userDB == null) {
			return null;
		}
		return repository.getAllOrdersMadeByUser(userDB.get());
	}

	@Override
	@Transactional
	public List<User> getUsersSpendingMoreThan(Float amount) {
		return repository.getUsersSpendingMoreThan(amount);
	}

	@Override
	@Transactional
	public List<Supplier> getTopNSuppliersInSentOrders(int n) {
		List<Supplier> list = this.repository.getTopNSuppliersInSentOrders(n);
		return list;
	}

	@Override
	@Transactional
	public List<Product> getTop10MoreExpensiveProducts() {
		List<Product> productList = this.repository.getTop10MoreExpensiveProducts();
		return productList;
	}

	@Override
	@Transactional
	public List<User> getTop6UsersMoreOrders() {
		List<User> users = this.repository.getTop6UsersMoreOrders();
		return users;
	}

	@Override
	@Transactional
	public List<Order> getCancelledOrdersInPeriod(Date startDate, Date endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Order> orders = this.repository.getCancelledOrdersInPeriod(sdf.format(startDate),sdf.format(endDate));
		return orders;
	}

	@Override
	@Transactional
	public List<Order> getPendingOrders() {
		List<Order> orders = this.repository.getPendingOrders();
		return orders;
	}

	@Override
	@Transactional
	public List<Order> getSentOrders() {
		List<Order> orders = this.repository.getSentOrders();
		return orders;
	}

	@Override
	@Transactional
	public List<Order> getDeliveredOrdersInPeriod(Date startDate, Date endDate) { // Parse Exception?
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Order> orders = this.repository.getDeliveredOrdersInPeriod(sdf.format(startDate),sdf.format(endDate));
		return orders;
	}

	@Override
	@Transactional
	public List<Order> getDeliveredOrdersForUser(String username) {
		List<Order> orders = this.repository.getDeliveredOrdersForUser(username);
		return orders;
	}

	@Override
	@Transactional
	public List<Order> getSentMoreOneHour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public List<Order> getDeliveredOrdersSameDay() {
		List<Order> list = this.repository.getDeliveredOrdersSameDay();
		return list;
	}

	@Override
	@Transactional
	public List<User> get5LessDeliveryUsers() {
		List<User> users = this.repository.get5LessDeliveryUsers();
		return users;
	}

	@Override
	@Transactional
	public Product getBestSellingProduct() {
		Product p = this.repository.getBestSellingProduct();
		return p;
	}

	@Override
	@Transactional
	public List<Product> getProductsOnePrice() {
		List<Product> list = this.repository.getProductsOnePrice();
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	@Transactional
	public List<Product> getProductIncreaseMoreThan100() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Supplier getSupplierLessExpensiveProduct() {
		Supplier supplier = this.repository.getSupplierLessExpensiveProduct();
		return supplier;
	}

	@Override
	@Transactional
	public List<Supplier> getSuppliersDoNotSellOn(Date day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public List<Product> getSoldProductsOn(Date day) {
		List<Product> list = this.repository.getSoldProductsOn(day);
		return list;
	}

	@Override
	@Transactional
	public List<Order> getOrdersCompleteMorethanOneDay() {
		List<Order> list = this.repository.getOrdersCompleteMorethanOneDay();
		return list;
	}

	@Override
	@Transactional
	public List<Object[]> getProductsWithPriceAt(Date day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return this.repository.getProductsWithPriceAt(sdf.format(day));
	}

	@Override
	@Transactional
	public List<Product> getProductsNotSold() {
		List<Product> list = this.repository.getProductsNotSold();
		return list;
	}

	@Override
	@Transactional
	public List<Order> getOrderWithMoreQuantityOfProducts(Date day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return this.repository.getOrderWithMoreQuantityOfProducts(sdf.format(day));
	}

}