package ar.edu.unlp.info.bd2.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

public class Order implements PersistentObject{
	
	@BsonId
	private ObjectId objectId;
	
	private List<OrderStatus> status;	

	private OrderStatus actualStatus;

	private List<ProductOrder> products;
	
	private double coordX;
	
	private double coordY;
	
	private String address;
	
	private Float amount;
	
	private Date date;
	
	@BsonIgnore
	private User client;
	
	@BsonIgnore
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
		this.amount = 0F;
	}	
	
	public Order() { }
	
	public Float getAmount() {
		return amount;
	}
	
	public List<OrderStatus> getStatus() {
		return this.status;
	}
	
	public void changeActualStatus(OrderStatus status) {
		if(this.getActualStatus() == null && status.getStatus() == "Pending") {
			this.status.add(status);
			this.actualStatus = status;
		}else {
			if(this.getActualStatus().getStatusObject().canChangeToStatus(status.getStatusObject())) {
				this.status.add(status);
				this.actualStatus = status;
			}
		}
		
	}
	
	public void setStatus(List<OrderStatus> status) {
		this.status = status;
	}
	
	
	public void setActualStatus(OrderStatus actualStatus) {
		this.actualStatus = actualStatus;
	}

	public OrderStatus getActualStatus() {
		return actualStatus;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
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
	
	public void setProducts(List<ProductOrder> products) {
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
	
	public void addProductOrder(ProductOrder productOrder) {
		this.products.add(productOrder);
		if (productOrder.getProduct().getPriceAt(this.getDate()) == null) {
			this.amount += productOrder.getProduct().getActualPrice().getPrice() * productOrder.getQuantity();
		}else {
			this.amount += productOrder.getProduct().getPriceAt(this.getDate()) * productOrder.getQuantity();
		}
//		if(productOrder.getProduct().getPriceAt(this.getDate())){
//			this.amount += productOrder.getProduct().getPriceAt(this.getDate()) * productOrder.getQuantity();
//		}else {
//			this.amount += productOrder.getProduct().getCurrentPrice().getPrice() * productOrder.getQuantity();
//		}
	}
	
//	//--- Metodos State ---
	
	public boolean canDeliver() {
		return this.getActualStatus().getStatusObject().canDeliver(this);
	}
	
	public boolean canCancel() {
		return this.getActualStatus().getStatusObject().canCancel(this);
	}
	
	public boolean canFinish() {
		return this.getActualStatus().getStatusObject().canFinish(this);
	}

	@Override
	public ObjectId getObjectId() {
		return objectId;
	}

	@Override
	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	
	

}
