package ar.edu.unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
	private Long id;
	private String name;
	private List<Price> prices;
	private Supplier supplier;
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
//		actualizo el precio
		this.updatePrice(price);
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
		return this.prices.get(this.prices.size()-1).getPrice();
	}
	
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
	public void updatePrice( Float price ) {
		Price p = new Price( price );
		this.prices.add(p);
		
	}
}
