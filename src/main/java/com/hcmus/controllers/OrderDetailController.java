package com.hcmus.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.hcmus.assemblers.OrderDetailModelAssembler;
import com.hcmus.exceptions.EmptyResourceException;
import com.hcmus.exceptions.IllegalException;
import com.hcmus.exceptions.ResourceNotFoundException;
import com.hcmus.model.OrderDetail;
import com.hcmus.repositories.OrderDetailRepository;

@RestController
@RequestMapping("/orderDetails")
public class OrderDetailController {
	private final OrderDetailRepository repository;
	private final OrderDetailModelAssembler assembler;
	
	public OrderDetailController(OrderDetailRepository repository, OrderDetailModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	@GetMapping
	public CollectionModel<EntityModel<OrderDetail>> all(){
		List<EntityModel<OrderDetail>> orderDetails = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(orderDetails, linkTo(methodOn(UserController.class).all()).withSelfRel());
	}
	
	@GetMapping("{id}")
	public EntityModel<OrderDetail> one(@PathVariable int id){
		if(id <= 0) {
			throw new IllegalException(id);
		}
		
		OrderDetail orderDetail = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
		return assembler.toModel(orderDetail);
	}
	
	@PostMapping
	public ResponseEntity<?> newOrderDetail(@RequestBody OrderDetail newOrderDetail){
		if(newOrderDetail == null) {
			throw new EmptyResourceException();
		}
		
		EntityModel<OrderDetail> entityModel = assembler.toModel(repository.save(newOrderDetail));
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@PutMapping("{id}")
	ResponseEntity<?> updateOrderDetail(@RequestBody OrderDetail newOrderDetail, @PathVariable int id){
		if(newOrderDetail == null) {
			throw new EmptyResourceException();
		}
		
		if(repository.findById(id) != null) {
			newOrderDetail.setId(id);
			OrderDetail updatedOrderDetail = repository.save(newOrderDetail);
			EntityModel<OrderDetail> entityModel = assembler.toModel(updatedOrderDetail);
			return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
		}else {
			throw new ResourceNotFoundException(id);
		}
	}
	
	@DeleteMapping("{id}")
	ResponseEntity<?> deleteOrderDetail(@PathVariable int id){
		if(id <= 0) {
			throw new IllegalException(id);
		}
		
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
