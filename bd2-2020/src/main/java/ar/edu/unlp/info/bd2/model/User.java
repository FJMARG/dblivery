package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

@Entity
public class User {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String password;
	private String username;
	private String name;
	
	@Type(type="date")
	private Date dateOfBirth;
	
//	las ordenes de los clientes
	@OneToMany(mappedBy="client")
	private List<Order> orders;
	
//	las ordenes de los repartidores
	@OneToMany(mappedBy="deliveryUser")
	private List<Order> deliverOrders;

	public User(String email, String password, String username, String name, Date dateOfBirth) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.orders = new ArrayList<Order>();
		this.deliverOrders = new ArrayList<Order>();
	}

	public User() {
		
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Order> getDeliverOrders() {
		return deliverOrders;
	}

	public void setDeliverOrders(List<Order> deliverOrders) {
		this.deliverOrders = deliverOrders;
	}
}