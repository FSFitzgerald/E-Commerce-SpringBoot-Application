package com.hcmus.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmus.assemblers.UserModelAssembler;
import com.hcmus.exceptions.EmptyResourceException;
import com.hcmus.exceptions.IllegalException;
import com.hcmus.exceptions.ResourceNotFoundException;
import com.hcmus.model.User;
import com.hcmus.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserRepository repository;
	private final UserModelAssembler assembler;

	public UserController(UserRepository repository, UserModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	@GetMapping
	public CollectionModel<EntityModel<User>> all(){
		List<EntityModel<User>> users = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
	}
	
	@GetMapping("{id}")
	public EntityModel<User> one(@PathVariable int id){
		if(id <= 0) {
			throw new IllegalException(id);
		}
		
		User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
		return assembler.toModel(user);
	}
	
	@PostMapping
	public ResponseEntity<?> newUser(@RequestBody User newUser){
		if(newUser == null) {
			throw new EmptyResourceException();
		}
		
		newUser.setCreatedAt(new Date());
		
		EntityModel<User> entityModel = assembler.toModel(repository.save(newUser));
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@PutMapping("{id}")
	ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable int id){
		if(newUser == null) {
			throw new EmptyResourceException();
		}
		
		if(repository.findById(id) != null) {
			newUser.setId(id);
			User updatedUser = repository.save(newUser);
			EntityModel<User> entityModel = assembler.toModel(updatedUser);
			return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
		}else {
			throw new ResourceNotFoundException(id);
		}
	}
	
	@DeleteMapping("{id}")
	ResponseEntity<?> deleteUser(@PathVariable int id){
		if(id <= 0) {
			throw new IllegalException(id);
		}
		
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
