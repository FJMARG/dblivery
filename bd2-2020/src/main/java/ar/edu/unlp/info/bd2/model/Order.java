package ar.edu.unlp.info.bd2.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
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
	
	@OneToMany(mappedBy ="order")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ProductOrder> products;
	@Column(nullable=false)
	private double coordX;
	@Column(nullable=false)
	private double coordY;
	@Column(nullable=false)
	private String address;
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
	}	
	public Order() {
		
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
		return this.getStatus().get( this.status.size() - 1).deliver(this);
	}
	public boolean send() {
		return this.getStatus().get( this.status.size() - 1).send(this);
	}
	public boolean cancel() {
		return this.getStatus().get( this.status.size() - 1).deliver(this);
	}
	
}
