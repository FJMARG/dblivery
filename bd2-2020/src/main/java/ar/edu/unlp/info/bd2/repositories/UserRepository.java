package ar.edu.unlp.info.bd2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ar.edu.unlp.info.bd2.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
		
	public Optional<User> findByEmail( String email );

	public Optional<User> findByUsername( String username );
}
