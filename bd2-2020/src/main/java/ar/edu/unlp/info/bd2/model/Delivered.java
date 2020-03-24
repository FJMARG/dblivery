package ar.edu.unlp.info.bd2.model;

public class Delivered extends OrderStatus {
	public Delivered() {
		super("Delivered",(byte)2);
	}
	public boolean deliver(Order o) {
		return false;
	}
	public boolean send(Order o) {
		return false;
	}
	public boolean cancel(Order o) {
		return false;
	}
}
