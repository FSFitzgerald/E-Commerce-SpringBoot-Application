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

import com.hcmus.assemblers.OrderModelAssembler;
import com.hcmus.exceptions.EmptyResourceException;
import com.hcmus.exceptions.IllegalException;
import com.hcmus.exceptions.ResourceNotFoundException;
import com.hcmus.model.Order;
import com.hcmus.repositories.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {
	private final OrderRepository repository;
	private final OrderModelAssembler assembler;
	
	public OrderController(OrderRepository repository, OrderModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}
	
	@GetMapping
	public CollectionModel<EntityModel<Order>> all(){
		List<EntityModel<Order>> orders = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
	}
	
	@GetMapping("{id}")
	public EntityModel<Order> one(@PathVariable int id){
		if(id <= 0) {
			throw new IllegalException(id);
		}
		
		Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
		return assembler.toModel(order);
	}
	
	@PostMapping
	public ResponseEntity<?> newOrder(@RequestBody Order newOrder){
		if(newOrder == null) {
			throw new EmptyResourceException();
		}
		
		newOrder.setOrderedAt(new Date());
		
		EntityModel<Order> entityModel = assembler.toModel(repository.save(newOrder));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@PutMapping("{id}")
	ResponseEntity<?> updateOrder(@RequestBody Order newOrder, @PathVariable int id){
		if(newOrder == null) {
			throw new EmptyResourceException();
		}
		
		if(repository.findById(id) != null) {
			newOrder.setId(id);
			Order updatedOrder = repository.save(newOrder);
			EntityModel<Order> entityModel = assembler.toModel(updatedOrder);
			return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
		}else {
			throw new ResourceNotFoundException(id);
		}
	}
	
	@DeleteMapping("{id}")
	ResponseEntity<?> deleteOrder(@PathVariable int id){
		if(id <= 0) {
			throw new IllegalException(id);
		}
		
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
