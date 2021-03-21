package com.hcmus.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.hcmus.controllers.ProductController;
import com.hcmus.model.OrderDetail;

@Component
public class OrderDetailModelAssembler implements RepresentationModelAssembler<OrderDetail, EntityModel<OrderDetail>> {

	@Override
	public EntityModel<OrderDetail> toModel(OrderDetail orderDetail) {
		return EntityModel.of(orderDetail,
				linkTo(methodOn(ProductController.class).one(orderDetail.getId())).withSelfRel(),
				linkTo(methodOn(ProductController.class).all()).withRel("products"));
	}

}
