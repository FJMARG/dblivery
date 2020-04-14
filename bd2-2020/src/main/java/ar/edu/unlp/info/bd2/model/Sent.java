package ar.edu.unlp.info.bd2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity
@DiscriminatorValue("Sent")
public class Sent extends OrderStatus {

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
	public boolean canChangeToStatus(OrderStatus s) {
		if(s.getStatus() == "Delivered") {
			return true;
		}
		return false;
	}

	
}
