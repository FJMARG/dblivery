package ar.edu.unlp.info.bd2.model;

import java.util.Date;

public class Price {
	private long id;
	private float price;
	private Date date;
	public Price(long id, float price, Date date) {
		super();
		this.id = id;
		this.price = price;
		this.date = date;
	}
	public Price() {
		
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
