package ar.edu.unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.OrderStatus;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.Supplier;
import ar.edu.unlp.info.bd2.model.User;
import ar.edu.unlp.info.bd2.repositories.DBliveryException;
import ar.edu.unlp.info.bd2.repositories.DBliveryMongoRepository;

public class DBliveryServiceImpl implements DBliveryService {

	private DBliveryMongoRepository repository;
	
	public DBliveryServiceImpl(DBliveryMongoRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Product createProduct(String name, Float price, Float weight, Supplier supplier) {
		Product product = new Product(name, price, supplier, weight);
		repository.insertInto("products", Product.class, product);
		return product;
	}

	@Override
	public Product createProduct(String name, Float price, Float weight, Supplier supplier, Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Supplier createSupplier(String name, String cuil, String address, Float coordX, Float coordY) {
		Supplier supplier = new Supplier(name, cuil, address, coordX, coordY);
		repository.insertInto("suppliers", Supplier.class, supplier);
		return supplier;
	}

	@Override
	public User createUser(String email, String password, String username, String name, Date dateOfBirth) {
		User user = new User(email, password, username, name, dateOfBirth);		
		repository.insertInto("users", User.class, user);
		return user;
	}

	@Override
	public Product updateProductPrice(ObjectId id, Float price, Date startDate) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> getUserById(ObjectId id) {
		User user = (User) this.repository.findById("users", User.class, id);
		return Optional.of(user);
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		User user = this.repository.getUserByField("email", email);
		return Optional.of(user);
	}

	@Override
	public Optional<User> getUserByUsername(String username) {
		User user = this.repository.getUserByField("username", username);
		return Optional.of(user);
	}

	@Override
	public Optional<Order> getOrderById(ObjectId id) {
		Order order = (Order) this.repository.findById("order", Order.class, id);
		return Optional.of(order);
	}

	@Override
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		Order order = new Order(client, coordX, coordY, address, new Date());		
//		esto está mal xq el cliente es un documento embebido, pero todavia no se como insertarlo
		repository.insertInto("order", Order.class, order);
		return order;
	}

	@Override
	public Order addProduct(ObjectId order, Long quantity, Product product) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order deliverOrder(ObjectId order, User deliveryUser) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order deliverOrder(ObjectId order, User deliveryUser, Date date) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order cancelOrder(ObjectId order) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order cancelOrder(ObjectId order, Date date) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order finishOrder(ObjectId order) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order finishOrder(ObjectId order, Date date) throws DBliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canCancel(ObjectId order) throws DBliveryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canFinish(ObjectId id) throws DBliveryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDeliver(ObjectId order) throws DBliveryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OrderStatus getActualStatus(ObjectId order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductsByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	

}