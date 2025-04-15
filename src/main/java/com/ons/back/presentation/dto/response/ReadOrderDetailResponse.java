package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Order;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReadOrderDetailResponse(
        Long orderId,
        LocalDateTime orderDate,
        List<ReadOrderItemResponse> orderItemResponseList,
        Double supplyAmount,
        Double vat,
        Double totalAmount
) {
    public static ReadOrderDetailResponse fromEntity(Order order) {
        return ReadOrderDetailResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getCreatedAt())
                .orderItemResponseList(order.getOrderItemList().stream().map(ReadOrderItemResponse::fromEntity).toList())
                .supplyAmount(order.getTotalAmount() * 10 / 11)
                .vat(order.getTotalAmount() * 1 / 11)
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
