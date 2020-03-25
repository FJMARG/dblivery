package ar.edu.unlp.info.bd2.model;

public class ProductOrder {
	private Long id;
	private Long quantity;
	private Product product;
	
	public ProductOrder(Long quantity, Product product) {
		super();
		this.quantity = quantity;
		this.product = product;
	}
	
	public ProductOrder() {
		
	}
	
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
