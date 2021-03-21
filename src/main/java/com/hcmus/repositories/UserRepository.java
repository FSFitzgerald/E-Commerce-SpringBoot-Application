package com.hcmus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmus.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User getUserByEmailAndPassword(String email, String password);
}
