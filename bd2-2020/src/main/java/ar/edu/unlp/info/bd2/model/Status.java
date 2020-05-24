package ar.edu.unlp.info.bd2.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

@BsonDiscriminator
public abstract class Status implements PersistentObject{
	
	@BsonId
	public ObjectId objectId;
	
	public abstract String getStatus();
	
	public abstract boolean canDeliver(Order o);
		
	public abstract boolean canCancel(Order o);
	
	public abstract boolean canFinish(Order o);	
	
	public abstract boolean canChangeToStatus(Status s);	
	
}
