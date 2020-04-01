package ar.edu.unlp.info.bd2.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class Price {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private Float price;
	
	@Type(type="date")
	@Column(nullable=false)
	private Date startDate;
	
	@Type(type="date")
	@Column
	private Date endDate;
	
	public Price(float price, Date startDate) {
		super();
		this.price = price;
		this.startDate = startDate;
	}
	public Price() {
		
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Long getId() {
		return id;
	}
	
}
