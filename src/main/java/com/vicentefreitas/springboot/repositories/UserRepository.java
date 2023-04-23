package com.vicentefreitas.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vicentefreitas.springboot.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByLogin(String login);

}
