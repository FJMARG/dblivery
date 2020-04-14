package ar.edu.unlp.info.bd2.services;

import java.util.Date;
import java.util.List;

import ar.edu.unlp.info.bd2.model.*;

public interface DBliveryStatisticsService {

	/**
	 * Obtiene todas las ordenes realizadas por el usuario con username <code>username</code>
	 * @param username
	 * @return Una lista de ordenes que satisfagan la condición
	 */
	List<Order> getAllOrdersMadeByUser(String username);

	/**
	 * Obtiene todos los usuarios que han gastando más de <code>amount</code> en alguna orden en la plataforma
	 * @param amount
	 * @return una lista de usuarios que satisfagan la condici{on
	 */
	List<User> getUsersSpendingMoreThan(Float amount);

	/**
	 * Obtiene los <code>n</code> proveedores que más productos tienen en ordenes que están siendo enviadas
	 * @param n
	 * @return una lista con los <code>n</code> proveedores que satisfagan la condición
	 */
	List<Supplier> getTopNSuppliersInSentOrders(int n);

	/**
	 * Obtiene los 9 productos más costosos
	 * @return una lista con los productos que satisfacen la condición
	 */
	List<Product> getTop10MoreExpensiveProducts();

	/**
	 * Obtiene los 6 usuarios que más cantidad de ordenes han realizado
	 * @return una lista con los usuarios que satisfacen la condición
	 */
	List<User> getTop6UsersMoreOrders();

	/**
	 * Obtiene todas las ordenes canceladas entre dos fechas
	 * @param startDate
	 * @param endDate
	 * @return una lista con las ordenes que satisfagan la condición
	 */
	List <Order> getCancelledOrdersInPeriod(Date startDate, Date endDate);

	/**
	 * Obtiene el listado de las ordenes pendientes
	 */
	List <Order>  getPendingOrders();

	/**
	 * Obtiene el listado de las ordenes enviadas y no entregadas
	 */
	List <Order>  getSentOrders();


	/**
	 * Obtiene todas las ordenes entregadas entre dos fechas
	 * @param startDate
	 * @param endDate
	 * @return una lista con las ordenes que satisfagan la condición
	 */
	List <Order> getDeliveredOrdersInPeriod(Date startDate, Date endDate);

	/**
	 * Obtiene todas las órdenes entregadas para el cliente con username <code>username</code>
	 * @param username
	 * @return una lista de ordenes que satisfagan la condición
	 */
	List <Order> getDeliveredOrdersForUser(String username);

	/**
	 * Obtiene las ordenes que fueron enviadas luego de una hora de realizadas (en realidad, luego de 24hs más tarde)
	 * @return una lista de ordenes que satisfagan la condición
	 */
	List <Order> getSentMoreOneHour();

	/**
	 * Obtiene las ordenes que fueron entregadas el mismo día de realizadas
	 * @return una lista de ordenes que satisfagan la condición
	 */
	List <Order> getDeliveredOrdersSameDay();

	/**
	 * Obtiene los 5 repartidores que menos ordenes tuvieron asignadas (tanto sent como delivered)
	 * @return una lista de las ordenes que satisfagan la condición
	 */
	List <User> get5LessDeliveryUsers();


	/**
	 * Obtiene el producto con más demanda
	 * @return el producto que satisfaga la condición
	 */
	Product getBestSellingProduct();

	/**
	 * Obtiene los productos que no cambiaron su precio
	 * @return una lista de productos que satisfagan la condición
	 */
	List<Product> getProductsOnePrice();

	/**
	 * Obtiene la lista de productos que han aumentado más de un 100% desde su precio inicial
	 * @return una lista de productos que satisfagan la condición
	 */
	List <Product> getProductIncreaseMoreThan100();

	/**
	 * Obtiene el proveedor con el producto de menor valor historico de la plataforma
	 * @return el proveedor que cumple la condición
	 */
	Supplier getSupplierLessExpensiveProduct();

	/**
	 * Obtiene los proveedores que no vendieron productos en un <code>day</code>
	 * @param day
	 * @return una lista de proveedores que cumplen la condición
	 */
	List <Supplier> getSuppliersDoNotSellOn(Date day);

	/**
	 * obtiene los productos vendidos en un <code>day</code>
	 * @param day
	 * @return una lista los productos vendidos
	 */
	List <Product> getSoldProductsOn(Date day);

	/**
	 * obtiene las ordenes que fueron entregadas en m{as de un día desde que fueron iniciadas(status pending)
	 * @return una lista de las ordenes que satisfagan la condición
	 */
	List<Order> getOrdersCompleteMorethanOneDay();

	/**
	 * obtiene el listado de productos con su precio a una fecha dada
	 * @param day
	 * @return la lista de cada producto con su precio en la fecha dada
	 */
	List <Object[]> getProductsWithPriceAt(Date day);

	/**
	 * obtiene la lista de productos que no se han vendido
	 * @return la lista de productos que satisfagan la condición
	 */
	List <Product> getProductsNotSold();


	/**
	 * obtiene la/s orden/es con mayor cantidad de productos ordenados de la fecha dada
	 * @day
	 * @return una lista con las ordenes que cumplen la condición
	 */
	List <Order> getOrderWithMoreQuantityOfProducts(Date day);
}