package ar.edu.unlp.info.bd2.model;


public class Pending extends OrderStatus {
	public Pending() {
		this.setStatus("Pending");
	}
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
}
