package ar.edu.unlp.info.bd2.model;

import java.util.Date;

public class Price {
	private Long id;
	private Float price;
	private Date startDate;
	private Date endDate;
	
	public Price(float price) {
		super();
		this.price = price;
		this.startDate = new Date();
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
