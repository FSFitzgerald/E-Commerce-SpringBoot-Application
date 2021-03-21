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

import com.hcmus.assemblers.ProductModelAssembler;
import com.hcmus.exceptions.EmptyResourceException;
import com.hcmus.exceptions.IllegalException;
import com.hcmus.exceptions.ResourceNotFoundException;
import com.hcmus.model.Product;
import com.hcmus.repositories.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {
	private final ProductRepository repository;
	private final ProductModelAssembler assembler;
	
	
	public ProductController(ProductRepository repository, ProductModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	@GetMapping
	public CollectionModel<EntityModel<Product>> all(){
		List<EntityModel<Product>> products = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(products, linkTo(methodOn(ProductController.class).all()).withSelfRel());
	}
	
	@GetMapping("{id}")
	public EntityModel<Product> one(@PathVariable int id){
		if(id <= 0) {
			throw new IllegalException(id);
		}
		
		Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
		return assembler.toModel(product);
	}
	
	@PostMapping
	public ResponseEntity<?> newProduct(@RequestBody Product newProduct){
		if(newProduct == null) {
			throw new EmptyResourceException();
		}
		
		newProduct.setCreatedAt(new Date());
		
		EntityModel<Product> entityModel = assembler.toModel(repository.save(newProduct));
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@PutMapping("{id}")
	ResponseEntity<?> updateProduct(@RequestBody Product newProduct, @PathVariable int id){
		if(newProduct == null) {
			throw new EmptyResourceException();
		}
		
		if(repository.findById(id) != null) {
			newProduct.setId(id);
			Product updatedProduct = repository.save(newProduct);
			EntityModel<Product> entityModel = assembler.toModel(updatedProduct);
			return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
		}else {
			throw new ResourceNotFoundException(id);
		}
	}
	
	@DeleteMapping("{id}")
	ResponseEntity<?> deleteProduct(@PathVariable int id){
		if(id <= 0) {
			throw new IllegalException(id);
		}
		
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
