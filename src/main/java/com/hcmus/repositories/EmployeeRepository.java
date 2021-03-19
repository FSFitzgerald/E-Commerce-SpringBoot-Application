package com.hcmus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
