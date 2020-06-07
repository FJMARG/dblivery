package ar.edu.unlp.info.bd2.model;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import ar.edu.unlp.info.bd2.mongo.PersistentObject;

public class OrderStatus {

	private Date date;

	private Status statusObject;
	
	public OrderStatus() {}

	public OrderStatus(Status statusObject) {
		super();
		this.statusObject = statusObject;
		this.date = new Date();
	}

	public OrderStatus(Date date, Status statusObject) {
		super();
		this.date = date;
		this.statusObject = statusObject;
	}

	public Date getDate() {
		return date;
	}

	public String getStatus() {
		return statusObject.getStatus();
	}
	
	public Status getStatusObject() {
		return statusObject;
	}

	public void setStatusObject(Status statusObject) {
		this.statusObject = statusObject;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	
}
