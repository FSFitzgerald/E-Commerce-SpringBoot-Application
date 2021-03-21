package com.hcmus.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;

public class ResourceNotFoundException extends RuntimeException{
	public ResourceNotFoundException(int id){
		super("Cound not found resource " + id);
	}
}
