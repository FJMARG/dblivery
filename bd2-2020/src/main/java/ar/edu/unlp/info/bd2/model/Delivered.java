package ar.edu.unlp.info.bd2.model;

public class Delivered extends OrderStatus {
	public Delivered() {
	}
	
	public String getStatus() {
		return "Delivered";
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
