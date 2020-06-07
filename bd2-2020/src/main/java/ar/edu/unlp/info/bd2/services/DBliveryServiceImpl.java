package ar.edu.unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.OrderStatus;
import ar.edu.unlp.info.bd2.model.Pending;
import ar.edu.unlp.info.bd2.model.Price;
import ar.edu.unlp.info.bd2.model.Product;
import ar.edu.unlp.info.bd2.model.ProductOrder;
import ar.edu.unlp.info.bd2.model.Sent;
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
		repository.insertProduct(product);
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
		Product product = (Product)this.repository.findById("products", Product.class, id);
		List<Price> priceList = this.repository.getAssociatedObjects(product, Price.class, "products_prices", "prices");
		product.setPrices(priceList);
		product.updatePrice(price, startDate);
		return this.repository.updateProductPrice(product);
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
		Order order = (Order) this.repository.findById("orders", Order.class, id);
		List<User> clientList = this.repository.getAssociatedObjects(order, User.class, "orders_clients", "users");
		order.setClient(clientList.get(0));
		List<User> deliveryUserList = this.repository.getAssociatedObjects(order, User.class, "orders_deliveryUsers", "users");
		if(!deliveryUserList.isEmpty())
			order.setDeliveryUser(deliveryUserList.get(0));
		return Optional.of(order);
	}

	@Override
	public Order createOrder(Date dateOfOrder, String address, Float coordX, Float coordY, User client) {
		Order order = new Order(client, coordX, coordY, address, new Date());
		order.changeActualStatus(new OrderStatus(new Pending()));
		repository.insertOrder(order);
		return order;
	}

	@Override
	public Order addProduct(ObjectId order, Long quantity, Product product) throws DBliveryException {
		Optional<Order> orderDB = this.getOrderById(order);
		if (orderDB.isEmpty())
			throw new DBliveryException("La orden no existe");
		
		ProductOrder po = new ProductOrder(quantity, product, orderDB.get()); 
		orderDB.get().addProductOrder(po);

		return (Order) repository.updateOn("orders", Order.class, orderDB.get());
	}

	@Override
	public Order deliverOrder(ObjectId order, User deliveryUser) throws DBliveryException {
		Optional<Order> orderDB = this.getOrderById(order);
		if (orderDB.isEmpty())
			throw new DBliveryException("La orden no existe");
		
		Optional<User> userDB = this.getUserById(deliveryUser.getObjectId());
		if (userDB.isEmpty())
			throw new DBliveryException("El usuario no existe");
		
		if( !orderDB.get().canDeliver() )
			throw new DBliveryException("The order can't be delivered");
	
    	repository.saveAssociation(orderDB.get(), deliveryUser, "orders_deliveryUsers");
		orderDB.get().changeActualStatus(new OrderStatus(new Sent()));
		repository.updateOn("orders", Order.class, orderDB.get());
		
		orderDB = this.getOrderById(order);
		return orderDB.isPresent() ? orderDB.get() : null;
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
		Optional<Order> orderDB = this.getOrderById(order);
		if (orderDB.isEmpty())
			throw new DBliveryException("La orden no existe");
		
		return orderDB.get().canDeliver();
	}

	@Override
	public OrderStatus getActualStatus(ObjectId order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductsByName(String name) {
		List<Product> products = this.repository.getProductsByName(name);
		return products;
	}

	

}