package ar.edu.unlp.info.bd2.repositories;

import org.springframework.data.repository.CrudRepository;

import ar.edu.unlp.info.bd2.model.ProductOrder;

public interface ProductOrderRepository extends CrudRepository<ProductOrder, Long> {

}
