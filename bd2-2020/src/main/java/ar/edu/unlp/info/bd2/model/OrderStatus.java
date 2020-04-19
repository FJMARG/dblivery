package ar.edu.unlp.info.bd2.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="OrderStatus")
public class OrderStatus {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Type(type="date")
	@Column(nullable=false)
	private Date date;
	
	@ManyToOne
	private Status status;
	
	public OrderStatus() {}

	public OrderStatus(Status status) {
		super();
		this.status = status;
		this.date = new Date();
	}

	public OrderStatus(Date date, Status status) {
		super();
		this.date = date;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	
}
