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
        String barcode,
        Boolean isOrdered,
        Boolean isSale,
        Double itemPurchasePrice
) {
    public static ReadItemResponse fromEntity(Item item) {
        return ReadItemResponse.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .itemStock(item.getItemStock())
                .itemPrice(item.getItemPrice())
                .itemImage(item.getItemImage())
                .barcode(item.getBarcode())
                .isOrdered(item.getIsOrdered())
                .isSale(item.getIsSale())
                .itemPurchasePrice(item.getItemPurchasePrice())
                .build();
    }
}
