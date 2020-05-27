package ar.edu.unlp.info.bd2.model;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

@BsonDiscriminator
public class Price implements PersistentObject{
	
	@BsonId
	private ObjectId objectId;

	private Float price;

	private Date startDate;

	private Date endDate;
	
	@BsonIgnore
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
	
	public void setPrice(Float price) {
		this.price = price;
	}
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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
