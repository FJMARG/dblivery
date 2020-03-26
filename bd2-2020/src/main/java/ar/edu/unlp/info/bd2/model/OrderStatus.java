package ar.edu.unlp.info.bd2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class OrderStatus {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public abstract String getStatus();
	
	public abstract boolean deliver(Order o);
	
	public abstract boolean send(Order o);
	
	public abstract boolean cancel(Order o);
	
}
