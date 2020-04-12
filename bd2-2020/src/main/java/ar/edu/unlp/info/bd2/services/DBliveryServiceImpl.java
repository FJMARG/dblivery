package ar.edu.unlp.info.bd2.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	public Product createProduct(String name, Float price, Float weight, Supplier supplier) {
		Product product = repository.getProductByName(name);
		if( product == null) {
			Product newProduct = new Product(name, price, supplier, weight);
			
			return repository.persist(newProduct);
		}else {
			return product;
		}
		
	}
	
	@Override
	public Product createProduct(String name, Float price, Float weight, Supplier supplier, Date sdf) {
		Product product = repository.getProductByName(name);
		if( product == null) {
			Product newProduct = new Product(name, price, supplier, weight, sdf);
			return repository.persist(newProduct);
		}else {
			return product;
		}
		
	}
		

	@Override
	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
		Supplier newSupplier = new Supplier(name, cuil, address, coordX, coordY);
		
		return repository.persist(newSupplier);
	}

	@Override
	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
		User user = repository.getUserByUsername(username);
		if (user != null) {
//			ya existe un usuario con ese username, lo devuelvo
			return user;
		}
		User newUser = new User(email, password, username, name, dateOfBirth);
		
		return repository.persist(newUser);
	}

	@Override
	public Product updateProductPrice(Long id, Float price, Date startDate) throws DBliveryException {
		Product product = repository.get(id, Product.class);
		if (product == null) {
			throw new DBliveryException("El producto solicitado no existe");
		}
		
		product.updatePrice(price, startDate);
		
		return repository.update(product);
	}

	@Override
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		Order newOrder = new Order(client, coordX, coordY, address, dateOfOrder);
//		busco el estado pending
		OrderStatus pending = this.createStatusIfNotExist(new Pending());
		
//		
		newOrder.setStatus(pending);
		return repository.persist(newOrder);
	}
	
	public OrderStatus createStatusIfNotExist(OrderStatus status) {
		OrderStatus o = repository.getStatusByName(status.getStatus());
		if (o == null) 
			o = repository.persist(status);
		
		return o;
	}
	
	@Override
	public Order addProduct(Long order, Long quantity, Product product) throws DBliveryException {
		Product p = repository.get(product.getId(), Product.class);
		Order updatedOrder = repository.get(order, Order.class);
		if (updatedOrder == null) {
			throw new DBliveryException("El pedido solicitado no existe");
		}
		
		ProductOrder po = new ProductOrder(quantity, p, updatedOrder);
		po = repository.persist(po);
		updatedOrder.getProducts().add(po);
		
		return repository.update(updatedOrder);
	}
	
	
	@Override
	public Optional<User> getUserById(Long id) {
		User u = repository.get(id, User.class);
		return Optional.of(u);
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		User u = repository.getUserByEmail(email);
		return Optional.of(u);
	}

	@Override
	public Optional<User> getUserByUsername(String username) {
		User u = repository.getUserByUsername(username);
		return Optional.of(u);
	}

	@Override
	public Optional<Product> getProductById(Long id) {
		Product product = repository.get(id, Product.class);
		return Optional.of(product);
	}

	@Override
	public Optional<Order> getOrderById(Long id) {
		Order order = repository.get(id, Order.class);
		return Optional.of(order);
	}
	
	@Override
	public OrderStatus getActualStatus(Long order) {
		Optional<Order> orderDB = this.getOrderById(order);
		if(orderDB == null) 
			return null;
		
		return orderDB.get().getActualStatus();
	}

	@Override
	public List<Product> getProductByName(String name) {
		List<Product> productList = repository.getProductsByName(name);
		return productList;
	}
	
	@Override
	public boolean canCancel(Long order) throws DBliveryException {
		Optional<Order> orderDB = this.getOrderById(order);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		return orderDB.get().canCancel();
	}

	@Override
	public boolean canFinish(Long id) throws DBliveryException {
		Optional<Order> orderDB = this.getOrderById(id);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		return orderDB.get().canFinish();
	}

	@Override
	public boolean canDeliver(Long order) throws DBliveryException {
		Optional<Order> orderDB = this.getOrderById(order);
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		return orderDB.get().canDeliver();
	}
	
	@Override
	public Order deliverOrder(Long order, User deliveryUser) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		User userDB   = repository.get(deliveryUser.getId(), User.class);
		
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		if(userDB == null)
			throw new DBliveryException("El usuario asignado no existe");
		
		if( !orderDB.canDeliver() )
			throw new DBliveryException("The order can't be delivered");
		
		OrderStatus sent = this.createStatusIfNotExist(new Sent());
		
		orderDB.setStatus(sent);
		orderDB.setDeliveryUser(userDB);
		
		return repository.update(orderDB);
	}
	
	@Override
	public Order deliverOrder(Long order, User deliveryUser, Date sdf) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		User userDB   = repository.get(deliveryUser.getId(), User.class);
		
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		if(userDB == null)
			throw new DBliveryException("El usuario asignado no existe");
		
		if( !orderDB.canDeliver() )
			throw new DBliveryException("The order can't be delivered");
		
		OrderStatus sent = this.createStatusIfNotExist(new Sent());
		
		orderDB.setStatus(sent);
		orderDB.setDeliveryUser(userDB);
		
		return repository.update(orderDB);
	}

	@Override
	public Order cancelOrder(Long order) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		if( !orderDB.canCancel() )
			throw new DBliveryException("The order can't be cancelled");
		
		OrderStatus cancelled = this.createStatusIfNotExist(new Cancelled());
		orderDB.setStatus(cancelled);
		
		return repository.update(orderDB);
	}
	
	@Override
	public Order cancelOrder(Long order, Date sdf) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		if( !orderDB.canCancel() )
			throw new DBliveryException("The order can't be cancelled");
		
		OrderStatus cancelled = this.createStatusIfNotExist(new Cancelled());
		orderDB.setStatus(cancelled);
		
		return repository.update(orderDB);
	}

	@Override
	public Order finishOrder(Long order) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		if( !orderDB.canFinish() )
			throw new DBliveryException("The order can't be finished");
		
		OrderStatus delivered = this.createStatusIfNotExist(new Delivered());
		orderDB.setStatus(delivered);
		
		return repository.update(orderDB);
	}
	
	@Override
	public Order finishOrder(Long order, Date sdf) throws DBliveryException {
		Order orderDB = repository.get(order, Order.class);
		
		if(orderDB == null) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		if( !orderDB.canFinish() )
			throw new DBliveryException("The order can't be finished");
		
		OrderStatus delivered = this.createStatusIfNotExist(new Delivered());
		orderDB.setStatus(delivered);
		
		return repository.update(orderDB);
	}


	@Override
	public List<Order> getAllOrdersMadeByUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<User> getUsersSpendingMoreThan(Float amount) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Supplier> getTopNSuppliersInSentOrders(int n) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Product> getTop10MoreExpensiveProducts() {
		List<Product> productList = this.repository.getTop10MoreExpensiveProducts();
		return productList;
	}


	@Override
	public List<User> getTop6UsersMoreOrders() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Order> getCancelledOrdersInPeriod(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Order> getPendingOrders() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Order> getSentOrders() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Order> getDeliveredOrdersInPeriod(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Order> getDeliveredOrdersForUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Order> getSentMoreOneHour() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Order> getDeliveredOrdersSameDay() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<User> get5LessDeliveryUsers() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Product getBestSellingProduct() {
		Product p = this.repository.getBestSellingProduct();
		return p;
	}


	@Override
	public List<Product> getProductsOnePrice() {
		List<Product> list = this.repository.getProductsOnePrice();
		// TODO Auto-generated method stub
		return list;
	}


	@Override
	public List<Product> getProductIncreaseMoreThan100() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Supplier getSupplierLessExpensiveProduct() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Supplier> getSuppliersDoNotSellOn(Date day) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Product> getSoldProductsOn(Date day) {
		List<Product> list = this.repository.getSoldProductsOn(day);
		return list;
	}


	@Override
	public List<Order> getOrdersCompleteMorethanOneDay() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Object[]> getProductsWithPriceAt(Date day) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Product> getProductsNotSold() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Order> getOrderWithMoreQuantityOfProducts(Date day) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	

}
