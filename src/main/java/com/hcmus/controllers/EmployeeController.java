package com.hcmus.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcmus.model.Employee;
import com.hcmus.repositories.EmployeeRepository;

@RestController
public class EmployeeController {
	
	private final EmployeeRepository employeeRepository;
	
	public EmployeeController(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@GetMapping("/employees")
	@ResponseBody
	List<Employee> all(){
		return employeeRepository.findAll();
	}
}
