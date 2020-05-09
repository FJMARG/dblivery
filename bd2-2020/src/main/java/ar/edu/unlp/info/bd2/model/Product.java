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
import org.joda.time.LocalDate;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String name;	
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy="product")
	@OrderBy("startDate")
	private List<Price> prices;
	
	@OneToOne
	private Price currentPrice;
	
	@ManyToOne(optional=false)
	private Supplier supplier;
	
	@Column(nullable=false)
	private Float weight;
//	el precio actual del producto es el ultimo agregado  a la coleccion
	
	
	public Product() {
		
	}
	
	
	public Product(String name, Float price, Supplier supplier, Float weight, Date date) {
		super();
		this.name = name;
		this.prices = new ArrayList<Price>();
		this.supplier = supplier;
		this.weight = weight;
		this.currentPrice = null;
		this.updatePrice(price, date);
	}
	
	
	public Price getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Price currentPrice) {
		this.currentPrice = currentPrice;
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
	
	public List<Price> getPrices() {
		return prices;
	}
	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}
	public Float getPrice() {
		return this.getCurrentPrice().getPrice();
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
		if ( this.currentPrice != null ) {
			Date temp = (Date)startDate.clone();
			temp = LocalDate.fromDateFields(temp).minusDays(1).toDate();
			this.getCurrentPrice().setEndDate(temp);
		}
		this.currentPrice = new Price(price, startDate, this);
		this.prices.add(this.currentPrice);
	}
}
