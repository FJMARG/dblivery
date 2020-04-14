package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Comparator;
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
import javax.persistence.OrderBy;

import org.joda.time.LocalDateTime;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String name;	
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name="product_id")
	@OrderBy("startDate")
	private List<Price> prices;
	
	@ManyToOne(optional=false)
	private Supplier supplier;
	
	@Column(nullable=false)
	private Float weight;
//	el precio actual del producto es el ultimo agregado  a la coleccion
	
	
	public Product() {
		
	}
	
	public Product(String name, Float price, Supplier supplier, Float weight) {
		super();
		this.name = name;
		this.prices = new ArrayList<Price>();
		this.supplier = supplier;
		this.weight = weight;
		this.prices.add(new Price(price, new Date()));
	}
	
	public Product(String name, Float price, Supplier supplier, Float weight, Date date) {
		super();
		this.name = name;
		this.prices = new ArrayList<Price>();
		this.supplier = supplier;
		this.weight = weight;
		this.prices.add(new Price(price, date));
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
	
	public Float getLastPrice() {
		Price price = this.prices.stream().sorted((p1,p2)->p2.getStartDate().compareTo(p1.getStartDate())).findFirst().get();
		return price.getPrice();
	}
	
	public Float getPriceAt(Date f) {
		Price price = this.prices.stream().filter(p -> f.after(p.getStartDate()) & (f.before(p.getEndDate()))).findFirst().get();
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
		Price lastprice = this.prices.stream().sorted((p1,p2)->p2.getStartDate().compareTo(p1.getStartDate())).findFirst().get();
		lastprice.setEndDate(startDate);
		this.prices.add(new Price(price, startDate));
	}
}
