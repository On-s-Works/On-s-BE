package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Item;
import com.ons.back.persistence.domain.Store;

public record CreateItemRequest(
        String itemName,
        Double itemPrice,
        Integer itemStock,
        Long storeId
) {
    public Item toEntity(Store store) {
        return Item.builder()
                .itemName(itemName)
                .itemPrice(itemPrice)
                .itemStock(itemStock)
                .store(store)
                .build();
    }
}
