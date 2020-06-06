package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.joda.time.LocalDate;

import ar.edu.unlp.info.bd2.mongo.PersistentObject;

public class Product implements PersistentObject{
	
	@BsonId
	private ObjectId objectId;

	private String name;	

	@BsonIgnore
	private List<Price> prices;

	private Price actualPrice;

	@BsonIgnore
	private Supplier supplier;
	
	private Float weight;
		
	public Product() {
		
	}
	
	public Product(String name, Float price, Supplier supplier, Float weight) {
		super();
		this.name = name;
		this.prices = new ArrayList<Price>();
		this.supplier = supplier;
		this.weight = weight;
		this.actualPrice = null;
		this.updatePrice(price, new Date());
	}
	
	public Product(String name, Float price, Supplier supplier, Float weight, Date date) {
		super();
		this.name = name;
		this.prices = new ArrayList<Price>();
		this.supplier = supplier;
		this.weight = weight;
		this.actualPrice = null;
		this.updatePrice(price, date);
	}
	
	
	public Price getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Price actualPrice) {
		this.actualPrice = actualPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Price> getPrices() {
		return prices;
	}
	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}
	public Float getPrice() {
		return this.getActualPrice().getPrice();
	}
	
	public Float getPriceAt(Date f) {
		Price price = this.prices.stream().filter(p -> f.after(p.getStartDate()) & (f.before(p.getEndDate()))).findFirst().orElse(null);
		if(price == null) {
			return null;
		}
		return price.getPrice();
	};
	
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	
// METODO PARA SETEAR EL PRECIO (AGREGA A LA COLECCION PRICES Y LO SETEA COMO PRECIO ACTUAL)
	public void updatePrice( Float price, Date startDate ) {
		if ( this.actualPrice != null ) {
			Date temp = (Date)startDate.clone();
			temp = LocalDate.fromDateFields(temp).minusDays(1).toDate();
			this.getActualPrice().setEndDate(temp);
		}
		this.actualPrice = new Price(price, startDate, this);
		this.prices.add(this.actualPrice);
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
