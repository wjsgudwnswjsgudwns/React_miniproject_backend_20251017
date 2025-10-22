package com.jhj.miniproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.jhj.miniproject.dto.OrderResponseDto;
import com.jhj.miniproject.entity.*;
import com.jhj.miniproject.service.OrderService;
import com.jhj.miniproject.service.SiteUserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private SiteUserService siteUserService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> data) {
        try {
            String userId = data.get("userId");
            String editionType = data.get("edition");
            String platformCode = data.get("platform");
            String price = data.get("price");
            String paymentMethod = data.get("paymentMethod");

            Order order = orderService.createOrder(userId, editionType, platformCode, price, paymentMethod);
            return ResponseEntity.ok(order);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("결제 처리 중 오류 발생: " + e.getMessage());
        }
    }
    
    @GetMapping("/myorder")
    public ResponseEntity<?> myorder(Authentication auth) {
    	
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }
    	
        try {
            List<OrderResponseDto> orders = orderService.getOrdersByUserId(auth.getName());
            
            if (orders.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(orders);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("주문 정보 조회 중 오류 발생: " + e.getMessage());
        }
    }
}
