package com.hcmus.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hcmus.controllers.ProductController;
import com.hcmus.model.Order;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

	@Override
	public EntityModel<Order> toModel(Order order) {
		return EntityModel.of(order,
				linkTo(methodOn(ProductController.class).one(order.getId())).withSelfRel(),
				linkTo(methodOn(ProductController.class).all()).withRel("products"));
	}

}
