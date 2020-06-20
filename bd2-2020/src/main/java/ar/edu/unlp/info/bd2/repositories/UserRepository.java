package ar.edu.unlp.info.bd2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ar.edu.unlp.info.bd2.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	@Query("FROM User WHERE email = :email")
	public Optional<User> findByEmail( @Param("email") String email );

	@Query("FROM User WHERE username = :username")
	public Optional<User> findByUsername( @Param("username") String username );
}
