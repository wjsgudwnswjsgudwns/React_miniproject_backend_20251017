package com.jhj.miniproject.dto;

import com.jhj.miniproject.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String userId;
    
    private String editionName;
    private String editionType;
    
    private String platformName;
    private String platformCode;
    
    private String paymentMethod;
    private String price;
    private String status;
    private LocalDateTime createdAt;
    
    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.userId = order.getUser().getUserId(); 
        
        this.editionName = order.getEdition().getName();
        this.editionType = order.getEdition().getType();
        
        this.platformName = order.getPlatform().getName();
        this.platformCode = order.getPlatform().getCode();
        
        this.paymentMethod = order.getPaymentMethod();
        this.price = order.getPrice();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
    }
}