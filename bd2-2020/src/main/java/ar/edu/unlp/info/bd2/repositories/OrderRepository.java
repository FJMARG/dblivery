package ar.edu.unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ar.edu.unlp.info.bd2.model.Order;
import ar.edu.unlp.info.bd2.model.User;

public interface OrderRepository extends CrudRepository<Order, Long> {

	public List<Order> findByClient(User user);
	
	@Query("SELECT o FROM Order AS o " +
			"JOIN o.currentStatus AS os " +
			"WHERE os.status.class = Pending")
	public List<Order> getPendingOrders();

	@Query("SELECT o FROM Order AS o " +
			"JOIN o.currentStatus AS os " +
			"WHERE os.status.class = Sent")
	public List<Order> getSentOrders();

	@Query("SELECT o FROM Order AS o " +
			"JOIN o.currentStatus AS os " +
			"WHERE os.status.class = Delivered AND o.client = :user")
	public List<Order> getDeliveredOrdersForUser(@Param("user") User user);
	
	@Query("SELECT o FROM Order AS o " +
			"JOIN o.currentStatus AS os " +
			"WHERE os.status.class = Delivered " +
			"AND o.date BETWEEN :startDate AND :endDate")
	public List<Order> getDeliveredOrdersInPeriod(
			@Param("startDate") Date startDate, 
			@Param("endDate") Date endDate);

}
