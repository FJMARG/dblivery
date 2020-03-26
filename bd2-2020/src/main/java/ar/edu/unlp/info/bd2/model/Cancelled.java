package ar.edu.unlp.info.bd2.model;

public class Cancelled extends OrderStatus {
	public Cancelled() {
		this.setStatus("Cancelled");
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
