package com.jhj.miniproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhj.miniproject.dto.OrderResponseDto;
import com.jhj.miniproject.entity.*;
import com.jhj.miniproject.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Autowired
    private EditionRepository editionRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(String userId, String editionType, String platformCode, String price, String paymentMethod) {
        // 유저 조회
        SiteUser user = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 에디션 조회
        Edition edition = editionRepository.findByType(editionType)
                .orElseThrow(() -> new RuntimeException("에디션 정보를 찾을 수 없습니다."));

        // 플랫폼 조회
        Platform platform = platformRepository.findByCode(platformCode)
                .orElseThrow(() -> new RuntimeException("플랫폼 정보를 찾을 수 없습니다."));

        // 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setEdition(edition);
        order.setPlatform(platform);
        order.setPrice(price);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("COMPLETED");

        return orderRepository.save(order);
    }
    
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByUserId(String userId) {
        List<Order> orders = orderRepository.findByUser_UserId(userId);
        
        // LAZY 로딩된 엔티티에 접근하여 DTO로 변환
        return orders.stream()
                     .map(OrderResponseDto::new) // DTO 생성자에서 LAZY 필드 접근
                     .collect(Collectors.toList());
    }
}
