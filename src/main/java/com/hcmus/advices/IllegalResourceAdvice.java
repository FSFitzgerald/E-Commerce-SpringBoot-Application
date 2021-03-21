package com.hcmus.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hcmus.exceptions.IllegalException;

@ControllerAdvice
public class IllegalResourceAdvice {
	@ResponseBody
	@ExceptionHandler(IllegalException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String employeeNotFoundHandler(IllegalException ex) {
		return ex.getMessage();
	}
}
