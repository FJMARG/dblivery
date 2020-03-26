package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity	
public class Supplier {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String cuil;
	private String address;
	private double coordX;
	private double coordY;
	
	@OneToMany(mappedBy="supplier")
	private List<Product> products;
	
	public Supplier() {
		
	}
	
	public Supplier(String name, String cuil, String address, double coordX, double coordY) {
		super();
		this.name = name;
		this.cuil = cuil;
		this.address = address;
		this.coordX = coordX;
		this.coordY = coordY;
		this.products = new ArrayList<Product>();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCuil() {
		return cuil;
	}
	public void setCuil(String cuil) {
		this.cuil = cuil;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getCoordX() {
		return coordX;
	}
	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}
	public double getCoordY() {
		return coordY;
	}
	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}
	public List<Product> getProducts() {
		return products;
	}
	
	
}
