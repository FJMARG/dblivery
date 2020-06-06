package ar.edu.unlp.info.bd2.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

public class Sent extends Status {

	@Override
	public String getStatus() {
		return "Sent";
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
		return true;
	}
	
	@Override
	public boolean canChangeToStatus(Status s) {
		if(s.getStatus() == "Delivered") {
			return true;
		}
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
