package ar.edu.unlp.info.bd2.model;

import java.util.Date;
import java.util.List;

public class Order {
	
	private long id;
	private OrderStatus status;
	private User client;
	private List<Product> products;
	private double coordX;
	private double coordY;
	private String address;
	private Date date;
	private User deliveryUser;
	// public Point coord;

	public Order(User client, List<Product> products, double coordX, double coordY, String address,
			Date date) {
		super();
		this.status = new Pending();
		this.client = client;
		this.products = products;
		this.coordX = coordX;
		this.coordY = coordY;
		this.address = address;
		this.date = date;
	}	
	public Order() {
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public User getClient() {
		return client;
	}
	public void setClient(User client) {
		this.client = client;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public double getCoordX() {
		return coordX;
	}
	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}
	public double getCoordY() {
		return coordY;
	}
	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public User getDeliveryUser() {
		return deliveryUser;
	}
	public void setDeliveryUser(User deliveryUser) {
		this.deliveryUser = deliveryUser;
	}
	
	//--- Metodos State ---
	
	public boolean deliver() {
		return this.getStatus().deliver(this);
	}
	public boolean send() {
		return this.getStatus().send(this);
	}
	public boolean cancel() {
		return this.getStatus().deliver(this);
	}
	
}
