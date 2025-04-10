package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Item;
import lombok.Builder;

@Builder
public record ReadItemResponse(
        String itemName,
        Integer itemStock,
        Double itemPrice,
        String itemImage,
        String barcode
) {
    public static ReadItemResponse fromEntity(Item item) {
        return ReadItemResponse.builder()
                .itemName(item.getItemName())
                .itemStock(item.getItemStock())
                .itemPrice(item.getItemPrice())
                .itemImage(item.getItemImage())
                .barcode(item.getBarcode())
                .build();
    }
}
