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
		Product newProduct = new Product(name, price, supplier, weight);
		
		return repository.persist(newProduct);
	}
		

	@Override
	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
		Supplier newSupplier = new Supplier(name, cuil, address, coordX, coordY);
		
		return repository.persist(newSupplier);
	}

	@Override
	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
		User newUser = new User(email, password, username, name, dateOfBirth);
		
		return repository.persist(newUser);
	}

	@Override
	public Product updateProductPrice(Long id, Float price, Date startDate) throws DBliveryException {
		Product product = repository.get(id, Product.class);
		
		product.updatePrice(price, startDate);
		
		return repository.update(product);
	}

	@Override
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		Order newOrder = new Order(client, coordX, coordY, address, dateOfOrder);
		
		return repository.persist(newOrder);
	}
	
	@Override
	public Order addProduct(Long order, Long quantity, Product product) throws DBliveryException {
		Product p = repository.get(product.getId(), Product.class);
		Order updatedOrder = repository.get(order, Order.class);
		
		if(p != null) {
			ProductOrder po = new ProductOrder(quantity, p, updatedOrder);
			po = repository.persist(po);
			updatedOrder.getProducts().add(po);
			updatedOrder = repository.update(updatedOrder);
		}
		return updatedOrder;
	}
	
	
	@Override
	public Optional<User> getUserById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Product> getProductById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Order> getOrderById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public OrderStatus getActualStatus(Long order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductByName(String name) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Order deliverOrder(Long order, User deliveryUser) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
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
