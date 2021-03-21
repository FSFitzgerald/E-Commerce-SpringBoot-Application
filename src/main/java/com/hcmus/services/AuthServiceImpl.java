package com.hcmus.services;

import org.springframework.stereotype.Service;

import com.hcmus.model.User;
import com.hcmus.repositories.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	
	
	public AuthServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public int login(String email, String password) {
		User user = userRepository.getUserByEmailAndPassword(email, password);
		if(user == null) {
			return 0;
		}
		return user.getId();
	}

}
