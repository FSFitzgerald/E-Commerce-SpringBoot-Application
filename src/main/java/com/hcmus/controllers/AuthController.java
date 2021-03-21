package com.hcmus.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcmus.model.User;
import com.hcmus.services.AuthService;

@RestController
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody User user){
		/*
		if(user == null || user.getEmail() == null || user.getPassword() == null) {
			throw new EmptyResourceException();
		}
		*/
		int id = authService.login(user.getEmail(), user.getPassword());
		return ResponseEntity.status(HttpStatus.OK).body(id);
	}
}
