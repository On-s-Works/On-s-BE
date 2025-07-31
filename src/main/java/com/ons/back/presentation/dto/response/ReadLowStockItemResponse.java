package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Item;
import lombok.Builder;

@Builder
public record ReadLowStockItemResponse(
        String itemImage,
        String itemName,
        String barcode,
        Double itemPrice,
        Integer itemStock,
        Boolean isOrdered,
        Boolean isSale
) {
    public static ReadLowStockItemResponse fromEntity(Item item) {
        return ReadLowStockItemResponse.builder()
                .itemImage(item.getItemImage())
                .itemName(item.getItemName())
                .barcode(item.getBarcode())
                .itemPrice(item.getItemPrice())
                .itemStock(item.getItemStock())
                .isOrdered(item.getIsOrdered())
                .isSale(item.getIsSale())
                .build();
    }
}
