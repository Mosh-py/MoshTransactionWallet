package com.moshood.mwta.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moshood.mwta.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	
}
