package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Item;
import lombok.Builder;

@Builder
public record ReadItemResponse(
        Long id,
        String itemName,
        Integer itemStock,
        Double itemPrice,
        String itemImage,
        String barcode
) {
    public static ReadItemResponse fromEntity(Item item) {
        return ReadItemResponse.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .itemStock(item.getItemStock())
                .itemPrice(item.getItemPrice())
                .itemImage(item.getItemImage())
                .barcode(item.getBarcode())
                .build();
    }
}
