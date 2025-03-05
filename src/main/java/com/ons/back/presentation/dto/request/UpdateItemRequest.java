package com.ons.back.presentation.dto.request;

public record UpdateItemRequest(
        Long itemId,
        String itemName,
        Double itemPrice,
        Integer itemStock
) {
}
