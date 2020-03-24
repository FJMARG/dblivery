package ar.edu.unlp.info.bd2.model;

public abstract class OrderStatus {
	// No lleva id al no ser parte del modelo?
	private String status;
	private byte size;
	// En los test case (por ejemplo, linea 169) se le envia el mensaje
	// size al estado. Lo unico que se me ocurrio fue esto pero suena muy
	// HardCoded. OrderStatus tendra una coleccion adentro y
	// guardara todos los estados por los que va pasando dicha orden?.
	
	public OrderStatus(String status, byte size) {
		super();
		this.status = status;
		this.size = size;
	}

	public byte size() {
		return size;
	}
	public String getStatus() {
		return this.status;
	}
	
	public abstract boolean deliver(Order o);
	
	public abstract boolean send(Order o);
	
	public abstract boolean cancel(Order o);
	
}
