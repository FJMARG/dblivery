package ar.edu.unlp.info.bd2.model;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import ar.edu.unlp.info.bd2.mongo.PersistentObject;

@BsonDiscriminator
public class OrderStatus implements PersistentObject{
	
	@BsonId
	private ObjectId objectId;

	private Date date;

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

	public Date getDate() {
		return date;
	}

	public String getStatus() {
		return status.getStatus();
	}

	public Status getStatusObject() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public ObjectId getObjectId() {
		return objectId;
	}

	@Override
	public void setObjectId(ObjectId objectId) {
		objectId = objectId;
		
	}

	
}
