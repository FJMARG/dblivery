package ar.edu.unlp.info.bd2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Pending")
public class Pending extends OrderStatus {

	@Override
	public String getStatus() {
		return "Pending";
	}

	@Override
	public boolean canDeliver(Order o) {
		if( o.getProducts().size() == 0 )
			return false;
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
	public boolean canChangeToStatus(OrderStatus s) {
		if((s.getStatus() == "Sent") ||  (s.getStatus() == "Cancelled")) {
			return true;
		}
		return false;
	}
	
}
