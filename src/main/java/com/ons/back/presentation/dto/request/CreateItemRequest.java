package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Item;
import com.ons.back.persistence.domain.Store;

import java.time.LocalDateTime;

public record CreateItemRequest(
        String itemName,
        Double itemPrice,
        Double itemPurchasePrice,
        String barcode,
        Long storeId
) {
    public Item toEntity(Store store, String itemImage) {
        return Item.builder()
                .itemName(itemName)
                .itemPrice(itemPrice)
                .itemPurchasePrice(itemPurchasePrice)
                .itemStock(0)
                .barcode(barcode)
                .itemImage(itemImage)
                .isActive(true)
                .isOrdered(false)
                .isSale(true)
                .store(store)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
