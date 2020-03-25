package ar.edu.unlp.info.bd2.model;

public class Sent extends OrderStatus {
	public Sent() {
		super("Sent",(byte)2);
	}
	
	public boolean deliver(Order o) {
		o.setStatus(new Delivered());
		return true;
	}
	public boolean send(Order o) {
		return false;
	}
	public boolean cancel(Order o) {
		return false;
	}
}
