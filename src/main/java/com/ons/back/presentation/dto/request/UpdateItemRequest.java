package com.ons.back.presentation.dto.request;

public record UpdateItemRequest(
        Long itemId,
        Integer itemStock,
        Boolean isOrdered,
        Long storeId
) {
}
