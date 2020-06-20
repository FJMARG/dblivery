package ar.edu.unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ar.edu.unlp.info.bd2.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	
	@Query("FROM Product WHERE name LIKE CONCAT('%',:name,'%')")
	public List<Product> findByName(@Param("name") String name);

}
