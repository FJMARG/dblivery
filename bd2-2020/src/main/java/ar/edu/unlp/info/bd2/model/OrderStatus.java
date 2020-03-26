package ar.edu.unlp.info.bd2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class OrderStatus {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String status;
	
	public String getStatus() {
		return this.status;
	};
	
	public void setStatus( String status ) {
		this.status = status;
	};
	public abstract boolean deliver(Order o);
	
	public abstract boolean send(Order o);
	
	public abstract boolean cancel(Order o);
	
}
