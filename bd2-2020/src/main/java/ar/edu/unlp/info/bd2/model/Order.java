package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	
	private Long id;
	private List<OrderStatus> status;
	private User client;
	private List<ProductOrder> products;
	private double coordX;
	private double coordY;
	private String address;
	private Date date;
	private User deliveryUser;

	public Order(User client, double coordX, double coordY, String address,Date date) {
		super();
		this.status = new ArrayList<OrderStatus>();
		this.client = client;
		this.coordX = coordX;
		this.coordY = coordY;
		this.address = address;
		this.date = date;
		this.products = new ArrayList<ProductOrder>();
	}	
	public Order() {
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public OrderStatus getStatus() {
		return this.status.get( this.status.size() - 1);
	}
	public void setStatus(OrderStatus status) {
		this.status.add(status);
	}
	public User getClient() {
		return client;
	}
	public void setClient(User client) {
		this.client = client;
	}
	public List<ProductOrder> getProducts() {
		return products;
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
