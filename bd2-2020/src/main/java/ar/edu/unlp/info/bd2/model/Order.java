package ar.edu.unlp.info.bd2.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

@Entity
@Table(name="Orders")
public class Order {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(	name="Order_OrderStatus",
				joinColumns=@JoinColumn(name="order_id",referencedColumnName="id"),
				inverseJoinColumns=@JoinColumn(name="status_id",referencedColumnName="id"))
	private List<OrderStatus> status;	
	
	@ManyToOne
	private OrderStatus currentStatus;

	public void setCurrentStatus(OrderStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	@OneToMany(mappedBy ="order")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ProductOrder> products;
	
	@Column(nullable=false)
	private double coordX;
	
	@Column(nullable=false)
	private double coordY;
	
	@Column(nullable=false)
	private String address;
	
	@Column(nullable=false)
	private Float amount;
	
	@Type(type="date")
	@Column(nullable=false)
	private Date date;
	
	@ManyToOne(optional=false)
	private User client;
	
	@ManyToOne(optional=true)
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
		this.currentStatus = null;
	}	
	
	public Order() { }
	
	public Float getAmount() {
//		Float amount = 0F;
//		for (ProductOrder productOrder : this.getProducts()) {
//			amount += productOrder.getProduct().getPrice() * productOrder.getQuantity();
//		}
		return amount;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public List<OrderStatus> getStatus() {
		return this.status;
	}
	
	public void setStatus(OrderStatus status) {
		if(this.getActualStatus() == null && status.getStatus() == "Pending") {
			this.status.add(status);
			this.setCurrentStatus(status);
		}else {
			if(this.getActualStatus().canChangeToStatus(status)) {
				this.status.add(status);
				this.setCurrentStatus(status);
			}
		}
		
	}
	
	public OrderStatus getActualStatus() {
		return this.currentStatus;
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
	
	public void addProductOrder(ProductOrder productOrder) {
		this.products.add(productOrder);
		this.amount += productOrder.getProduct().getPriceAt(this.date) * productOrder.getQuantity();
	}
	
	//--- Metodos State ---
	
	public boolean canDeliver() {
		return this.getActualStatus().canDeliver(this);
	}
	
	public boolean canCancel() {
		return this.getActualStatus().canCancel(this);
	}
	
	public boolean canFinish() {
		return this.getActualStatus().canFinish(this);
	}

	
	

}
