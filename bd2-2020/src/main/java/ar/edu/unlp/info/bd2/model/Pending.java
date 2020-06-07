package ar.edu.unlp.info.bd2.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

public class Pending extends Status {

	@Override
	public String getStatus() {
		return "Pending";
	}

	@Override
	public boolean canDeliver(Order o) {
//		if( o.getProducts().size() == 0 )
//			return false;
		return true;
	}

	@Override
	public boolean canCancel(Order o) {
		return true;
	}

	@Override
	public boolean canFinish(Order o) {
		return false;
	}

	@Override
	public boolean canChangeToStatus(Status s) {
		if((s.getStatus() == "Sent") ||  (s.getStatus() == "Cancelled")) {
			return true;
		}
		return false;
	}

	
}
