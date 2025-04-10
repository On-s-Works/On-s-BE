package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Item;
import lombok.Builder;

@Builder
public record ReadSaledItemResponse(
        String itemName,
        String itemImage,
        String barcode,
        Double itemSalePrice,
        Integer saleQuantity,
        Double totalPrice
) {
    public static ReadSaledItemResponse from(Item item, Integer saleQuantity) {
        return ReadSaledItemResponse.builder()
                .itemName(item.getItemName())
                .itemImage(item.getItemImage())
                .barcode(item.getBarcode())
                .itemSalePrice(item.getItemPrice())
                .saleQuantity(saleQuantity)
                .totalPrice(item.getItemPrice() * saleQuantity)
                .build();
    }
}
