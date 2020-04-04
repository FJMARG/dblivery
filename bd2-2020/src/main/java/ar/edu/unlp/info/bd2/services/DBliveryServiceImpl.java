package ar.edu.unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Propagation;
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
		if (o == null) {;
			o = repository.persist(status);
		}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductByName(String name) {
		List<Product> productLsist = repository.getProductsByName(name);
		return productLsist;
	}
	
	@Override
	public boolean canCancel(Long order) throws DBliveryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canFinish(Long id) throws DBliveryException {
		// TODO Auto-generated method stub
		return false;
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
		
		orderDB.send();
		orderDB.setDeliveryUser(userDB);
		this.createStatusIfNotExist(orderDB.getActualStatus());
		return repository.update(orderDB);
	}

	@Override
	public Order cancelOrder(Long order) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order finishOrder(Long order) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
