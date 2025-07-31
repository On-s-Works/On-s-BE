package com.ons.back.presentation.dto.request;

public record UpdateItemRequest(
        Long itemId,
        Integer itemStock,
        Boolean isOrdered,
        String barcode,
        Double itemPurchasePrice,
        Double itemPrice,
        Boolean isSale,
        Long storeId
) {
}
