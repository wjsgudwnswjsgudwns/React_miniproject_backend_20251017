package com.jhj.miniproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.miniproject.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	public List<Order> findByUser_UserId(String userId);
}
