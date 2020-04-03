package ar.edu.unlp.info.bd2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Pending")
public class Pending extends OrderStatus {
	public boolean deliver(Order o) {
		return false;
	}
	public boolean send (Order o) {
		if(o.getProducts().size()==0)
			return false;
		o.setStatus(new Sent());
		return true;
	}
	public boolean cancel(Order o) {
		o.setStatus(new Cancelled());
		return true;
	}
	@Override
	public String getStatus() {
		return "Pending";
	}
}
