package ar.edu.unlp.info.bd2.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

public class Delivered extends Status {

	@Override
	public String getStatus() {
		return "Delivered";
	}

	@Override
	public boolean canDeliver(Order o) {
		return false;
	}

	@Override
	public boolean canCancel(Order o) {
		return false;
	}

	
	@Override
	public boolean canFinish(Order o) {
		return false;
	}

	@Override
	public boolean canChangeToStatus(Status s) {
		return false;
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
