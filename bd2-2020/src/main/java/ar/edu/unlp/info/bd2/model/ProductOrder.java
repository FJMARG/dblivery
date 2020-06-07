package ar.edu.unlp.info.bd2.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

public class ProductOrder {
	
	private Long quantity;

	@BsonIgnore
	private Order order;

	private Product product;
	
	public ProductOrder(Long quantity, Product product, Order order) {
		super();
		this.quantity = quantity;
		this.product = product;
		this.order = order;
	}

	public ProductOrder() {	}
	

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Long getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}


}
