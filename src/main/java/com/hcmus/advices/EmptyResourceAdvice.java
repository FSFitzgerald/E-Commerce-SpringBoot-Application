package com.hcmus.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hcmus.exceptions.EmptyResourceException;

@ControllerAdvice
public class EmptyResourceAdvice {
	@ResponseBody
	@ExceptionHandler(EmptyResourceException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String employeeNotFoundHandler(EmptyResourceException ex) {
		return ex.getMessage();
	}
}
