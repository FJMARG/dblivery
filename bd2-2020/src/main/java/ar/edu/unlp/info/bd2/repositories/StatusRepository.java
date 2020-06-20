package ar.edu.unlp.info.bd2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ar.edu.unlp.info.bd2.model.Status;

public interface StatusRepository extends CrudRepository<Status, Long> {
	
	@Query("FROM Status WHERE status = :status")
	public Optional<Status> getStatusByName(@Param("status") String status);

}
