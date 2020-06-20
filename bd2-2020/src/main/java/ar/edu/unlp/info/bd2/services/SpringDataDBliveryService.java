package ar.edu.unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.OrderStatus;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.Supplier;
import ar.edu.unlp.info.bd2.model.User;
import ar.edu.unlp.info.bd2.repositories.DBliveryException;
import ar.edu.unlp.info.bd2.repositories.ProductRepository;
import ar.edu.unlp.info.bd2.repositories.SupplierRepository;
import ar.edu.unlp.info.bd2.repositories.UserRepository;

public class SpringDataDBliveryService implements DBliveryService, DBliveryStatisticsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
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
		// TODO Auto-generated method stub
		return null;
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
	public Optional<Order> getOrderById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order addProduct(Long order, Long quantity, Product product) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order deliverOrder(Long order, User deliveryUser) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order deliverOrder(Long order, User deliveryUser, Date date) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order cancelOrder(Long order) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order cancelOrder(Long order, Date date) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order finishOrder(Long order) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order finishOrder(Long order, Date date) throws DBliveryException {
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
	public OrderStatus getActualStatus(Long order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductsByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
