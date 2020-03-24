package ar.edu.unlp.info.bd2.model;

import java.util.List;

public class Product {
	private long id;
	private String name;
	private long price;
	private List<Price> prices;
	private Supplier supplier;
	private long weight;
	public Product() {
		
	}
	public Product(long id, String name, long price, List<Price> prices, Supplier supplier, long weight) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.prices = prices;
		this.supplier = supplier;
		this.weight = weight;
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
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public List<Price> getPrices() {
		return prices;
	}
	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public long getWeight() {
		return weight;
	}
	public void setWeight(long weight) {
		this.weight = weight;
	}
}
