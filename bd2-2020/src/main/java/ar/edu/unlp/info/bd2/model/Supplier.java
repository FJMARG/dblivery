package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

@BsonDiscriminator
public class Supplier implements PersistentObject{
	
	@BsonId
	private ObjectId objectId;

	private String name;
	
	private String cuil;

	private String address;

	private double coordX;

	private double coordY;

	@BsonIgnore
	private List<Product> products;
	
	public Supplier() {	}
	
	public Supplier(String name, String cuil, String address, double coordX, double coordY) {
		super();
		this.name = name;
		this.cuil = cuil;
		this.address = address;
		this.coordX = coordX;
		this.coordY = coordY;
		this.products = new ArrayList<Product>();
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

	@Override
	public ObjectId getObjectId() {
		return objectId;
	}

	@Override
	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}
	
	
}
