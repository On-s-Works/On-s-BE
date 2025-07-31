package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.OrderItem;
import lombok.Builder;

@Builder
public record ReadOrderItemResponse(
        Long orderItemId,
        Integer quantity,
        Double subtotal,
        String itemName,
        Double itemPrice,
        Boolean isSale
) {
    public static ReadOrderItemResponse fromEntity(OrderItem orderItem) {
        return ReadOrderItemResponse.builder()
                .orderItemId(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .subtotal(orderItem.getSubtotal())
                .itemName(orderItem.getItem().getItemName())
                .itemPrice(orderItem.getItem().getItemPrice())
                .isSale(orderItem.getItem().getIsSale())
                .build();
    }
}
