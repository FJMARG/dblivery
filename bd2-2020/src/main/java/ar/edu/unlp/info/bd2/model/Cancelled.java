package ar.edu.unlp.info.bd2.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

public class Cancelled extends Status {

	@Override
	public String getStatus() {
		return "Cancelled";
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
		if(s.getStatus() == "Pending") {
			return true;
		}
		return false;
	}


	
}
