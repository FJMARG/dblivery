package ar.edu.unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unlp.info.bd2.model.Cancelled;
import ar.edu.unlp.info.bd2.model.Delivered;
import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.OrderStatus;
import ar.edu.unlp.info.bd2.model.Pending;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.ProductOrder;
import ar.edu.unlp.info.bd2.model.Sent;
import ar.edu.unlp.info.bd2.model.Status;
import ar.edu.unlp.info.bd2.model.Supplier;
import ar.edu.unlp.info.bd2.model.User;
import ar.edu.unlp.info.bd2.repositories.DBliveryException;
import ar.edu.unlp.info.bd2.repositories.OrderRepository;
import ar.edu.unlp.info.bd2.repositories.OrderStatusRepository;
import ar.edu.unlp.info.bd2.repositories.ProductOrderRepository;
import ar.edu.unlp.info.bd2.repositories.ProductRepository;
import ar.edu.unlp.info.bd2.repositories.StatusRepository;
import ar.edu.unlp.info.bd2.repositories.SupplierRepository;
import ar.edu.unlp.info.bd2.repositories.UserRepository;

public class SpringDataDBliveryService implements DBliveryService, DBliveryStatisticsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderStatusRepository orderStatusRepository;
	
	@Autowired
	private ProductOrderRepository productOrderRepository;
	
	@Override
	public Product getMaxWeigth() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> getAllOrdersMadeByUser(String username) {
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
	public List<Product> getProductsOnePrice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getSoldProductsOn(Date day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Product createProduct(String name, Float price, Float weight, Supplier supplier) {
		Product newProduct = new Product(name, price, supplier, weight, new Date());
		return productRepository.save(newProduct);
	}

	@Override
	public Product createProduct(String name, Float price, Float weight, Supplier supplier, Date date) {
		Product newProduct = new Product(name, price, supplier, weight, date);
		return productRepository.save(newProduct);
	}

	@Override
	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
		Supplier newSupplier = new Supplier(name, cuil, address, coordX, coordY);
		return supplierRepository.save(newSupplier);
	}

	@Override
	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
		User newUser = new User(email, password, username, name, dateOfBirth);
		return userRepository.save(newUser);
	}

	@Override
	public Product updateProductPrice(Long id, Float price, Date startDate) throws DBliveryException {
		Optional<Product> productOpt = productRepository.findById(id);
		if(productOpt.isEmpty())
			throw new DBliveryException("El producto solicitado no existe");
		Product product = productOpt.get();
		product.updatePrice(price, startDate);
		return productRepository.save(product);			
	}

	@Override
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	@Transactional
	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	@Transactional
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		Order newOrder = new Order(client, coordX, coordY, address, dateOfOrder);
		//busco el estado pending
		Status pending = this.createStatusIfNotExist(new Pending());
		OrderStatus newOrderStatus = new OrderStatus(dateOfOrder, pending);
		newOrderStatus = orderStatusRepository.save(newOrderStatus);
		newOrder.setStatus(newOrderStatus);
		return orderRepository.save(newOrder);
	}

	@Override
	@Transactional
	public Order addProduct(Long order, Long quantity, Product product) throws DBliveryException {
		Optional<Order> orderOpt = orderRepository.findById(order);
		Optional<Product> productOpt = productRepository.findById(product.getId()); 
		if (orderOpt.isEmpty()) {
			throw new DBliveryException("El pedido solicitado no existe");
		}
		if (productOpt.isEmpty()) {
			throw new DBliveryException("El producto no existe");
		}
		Order updatedOrder = orderOpt.get();		
		
		ProductOrder po = new ProductOrder(quantity, productOpt.get(), updatedOrder);
		po = productOrderRepository.save(po);
		updatedOrder.addProductOrder(po);
		return orderRepository.save(updatedOrder);
	}

	@Override
	public Order deliverOrder(Long order, User deliveryUser) throws DBliveryException {
		Optional<Order> orderOpt = this.getOrderById(order);
		Optional<User> userOpt = this.getUserById(deliveryUser.getId());
		if(orderOpt.isEmpty()) 
			throw new DBliveryException("El pedido solicitado no existe");
		if(userOpt.isEmpty())
			throw new DBliveryException("El usuario asignado no existe");
		
		Order orderDB = orderOpt.get();
		if( !orderDB.canDeliver() )
			throw new DBliveryException("The order can't be delivered");
		
		Status sent = this.createStatusIfNotExist(new Sent());
		OrderStatus newOrderStatus = new OrderStatus(new Date(), sent);
		newOrderStatus = orderStatusRepository.save(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		orderDB.setDeliveryUser(userOpt.get());
		return orderRepository.save(orderDB);
	}

	@Override
	public Order deliverOrder(Long order, User deliveryUser, Date date) throws DBliveryException {
		Optional<Order> orderOpt = this.getOrderById(order);
		Optional<User> userOpt = this.getUserById(deliveryUser.getId());
		if(orderOpt.isEmpty()) 
			throw new DBliveryException("El pedido solicitado no existe");
		if(userOpt.isEmpty())
			throw new DBliveryException("El usuario asignado no existe");
		
		Order orderDB = orderOpt.get();
		if( !orderDB.canDeliver() )
			throw new DBliveryException("The order can't be delivered");
		
		Status sent = this.createStatusIfNotExist(new Sent());
		OrderStatus newOrderStatus = new OrderStatus(date, sent);
		newOrderStatus = orderStatusRepository.save(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		orderDB.setDeliveryUser(userOpt.get());
		return orderRepository.save(orderDB);
	}

	@Override
	public Order cancelOrder(Long order) throws DBliveryException {
		Optional<Order> orderOpt = this.getOrderById(order);
		if(orderOpt.isEmpty()) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		Order orderDB = orderOpt.get();
		if( !orderDB.canCancel() )
			throw new DBliveryException("The order can't be cancelled");
		
		Status cancelled = this.createStatusIfNotExist(new Cancelled());
		OrderStatus newOrderStatus = new OrderStatus(new Date(), cancelled);
		newOrderStatus = orderStatusRepository.save(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		return orderRepository.save(orderDB);
	}

	@Override
	public Order cancelOrder(Long order, Date date) throws DBliveryException {
		Optional<Order> orderOpt = this.getOrderById(order);
		if(orderOpt.isEmpty()) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		Order orderDB = orderOpt.get();
		if( !orderDB.canCancel() )
			throw new DBliveryException("The order can't be cancelled");
		
		Status cancelled = this.createStatusIfNotExist(new Cancelled());
		OrderStatus newOrderStatus = new OrderStatus(date, cancelled);
		newOrderStatus = orderStatusRepository.save(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		return orderRepository.save(orderDB);
	}

	@Override
	public Order finishOrder(Long order) throws DBliveryException {
		Optional<Order> orderOpt = this.getOrderById(order);
		if(orderOpt.isEmpty()) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		Order orderDB = orderOpt.get();
		if( !orderDB.canFinish() )
			throw new DBliveryException("The order can't be finished");
		
		Status delivered = this.createStatusIfNotExist(new Delivered());
		OrderStatus newOrderStatus = new OrderStatus(new Date(), delivered);
		newOrderStatus = orderStatusRepository.save(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		return orderRepository.save(orderDB);
	}

	@Override
	public Order finishOrder(Long order, Date date) throws DBliveryException {
		Optional<Order> orderOpt = this.getOrderById(order);
		if(orderOpt.isEmpty()) 
			throw new DBliveryException("El pedido solicitado no existe");
		
		Order orderDB = orderOpt.get();
		if( !orderDB.canFinish() )
			throw new DBliveryException("The order can't be finished");
		
		Status delivered = this.createStatusIfNotExist(new Delivered());
		OrderStatus newOrderStatus = new OrderStatus(date, delivered);
		newOrderStatus = orderStatusRepository.save(newOrderStatus);
		orderDB.setStatus(newOrderStatus);
		return orderRepository.save(orderDB);
	}

	@Override
	public boolean canCancel(Long order) throws DBliveryException {
		Optional<Order> orderOpt = this.getOrderById(order);
		if(orderOpt.isEmpty())
			throw new DBliveryException("El pedido solicitado no existe");
		return orderOpt.get().canCancel();
	}

	@Override
	public boolean canFinish(Long id) throws DBliveryException {
		Optional<Order> orderOpt = this.getOrderById(id);
		if(orderOpt.isEmpty())
			throw new DBliveryException("El pedido solicitado no existe");
		return orderOpt.get().canFinish();
	}

	@Override
	public boolean canDeliver(Long order) throws DBliveryException {
		Optional<Order> orderOpt = this.getOrderById(order);
		if(orderOpt.isEmpty())
			throw new DBliveryException("El pedido solicitado no existe");
		return orderOpt.get().canDeliver();
	}

	@Override
	public OrderStatus getActualStatus(Long order) {
		Optional<Order> orderOpt = this.getOrderById(order);
		if(orderOpt.isEmpty()) 
			return null;	
		return orderOpt.get().getActualStatus();
	}

	@Override
	public List<Product> getProductsByName(String name) {
		return productRepository.findByName(name);
	}
	
	@Transactional
	public Status createStatusIfNotExist(Status status) {
		Optional<Status> opt = statusRepository.getStatusByName(status.getStatus());
		if (opt.isEmpty()) 
			return statusRepository.save(status);
		return opt.get();
	}
}
