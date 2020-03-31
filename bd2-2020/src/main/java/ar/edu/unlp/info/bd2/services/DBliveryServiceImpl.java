package ar.edu.unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.OrderStatus;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.Supplier;
import ar.edu.unlp.info.bd2.model.User;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product updateProductPrice(Long id, Float price, Date startDate) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
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
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Order> getOrderById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Order addProduct(Long order, Long quantity, Product product) throws DBliveryException {
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
