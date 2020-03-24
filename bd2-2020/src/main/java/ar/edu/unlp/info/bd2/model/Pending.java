package ar.edu.unlp.info.bd2.model;

public class Pending extends OrderStatus {
	public Pending() {
		super("Pending",(byte)1);
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
