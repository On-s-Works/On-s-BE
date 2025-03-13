package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Order;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadOrderResponse(
        Long id,
        LocalDateTime orderDate,
        Double totalAmount
) {
    public static ReadOrderResponse fromEntity(Order order) {
        return ReadOrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getCreatedAt())
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
