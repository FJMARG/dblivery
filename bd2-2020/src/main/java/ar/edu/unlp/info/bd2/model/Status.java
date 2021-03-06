package ar.edu.unlp.info.bd2.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="status",discriminatorType = DiscriminatorType.STRING)
public abstract class Status {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public abstract String getStatus();
	
	public abstract boolean canDeliver(Order o);
		
	public abstract boolean canCancel(Order o);
	
	public abstract boolean canFinish(Order o);	
	
	public abstract boolean canChangeToStatus(Status s);	
	
}
