package ar.edu.unlp.info.bd2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Delivered")
public class Delivered extends OrderStatus {

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

}
