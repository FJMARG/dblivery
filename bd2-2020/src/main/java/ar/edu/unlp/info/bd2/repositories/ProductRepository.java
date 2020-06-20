package ar.edu.unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ar.edu.unlp.info.bd2.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	
	@Query("FROM Product WHERE name LIKE CONCAT('%',:name,'%')")
	public List<Product> findByName(@Param("name") String name);
	
	@Query("SELECT prod FROM Product AS prod " + 
			"JOIN prod.prices AS pr " +
			"GROUP BY prod " +
			"HAVING COUNT(*) = 1")
	public List<Product> getProductsOnePrice();
	
	@Query("SELECT prod FROM Order AS o " + 
			"JOIN o.products AS po " +
			"JOIN po.product AS prod " +
			"WHERE o.date = :day")
	public List<Product> getSoldProductsOn(@Param("day") Date day);
	
	@Query("FROM Product ORDER BY weight DESC")
	public Product getMaxWeight(PageRequest pageRequest);

}
