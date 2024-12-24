package org.example.webapptest.user;

import org.example.webapptest.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);
	
	List<User> findByDateOfBirth(Date  dateOfBirth);
}


