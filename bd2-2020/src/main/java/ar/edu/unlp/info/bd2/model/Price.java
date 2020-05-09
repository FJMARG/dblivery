package ar.edu.unlp.info.bd2.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Price {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private Float price;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date endDate;
	
	@ManyToOne
	private Product product;
	
	public Price(float price, Date startDate, Product product) {
		super();
		this.price = price;
		this.startDate = startDate;
		this.endDate = new Date(92233720368547L);
		this.product = product;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
}
