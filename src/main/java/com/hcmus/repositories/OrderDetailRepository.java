package com.hcmus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

}
