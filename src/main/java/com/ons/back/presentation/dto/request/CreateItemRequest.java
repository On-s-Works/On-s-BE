package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Item;
import com.ons.back.persistence.domain.Store;

public record CreateItemRequest(
        String itemName,
        Double itemPrice,
        Double itemPurchasePrice,
        String barcode,
        String itemImage,
        Long storeId
) {
    public Item toEntity(Store store) {
        return Item.builder()
                .itemName(itemName)
                .itemPrice(itemPrice)
                .itemPurchasePrice(itemPurchasePrice)
                .itemStock(0)
                .barcode(barcode)
                .itemImage(itemImage)
                .isActive(true)
                .isOrdered(false)
                .store(store)
                .build();
    }
}
