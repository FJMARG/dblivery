package ar.edu.unlp.info.bd2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity
@DiscriminatorValue("Cancelled")
public class Cancelled extends OrderStatus {

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
	public boolean canChangeToStatus(OrderStatus s) {
		if(s.getStatus() == "Pending") {
			return true;
		}
		return false;
	}

	
}
