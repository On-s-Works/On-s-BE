package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.type.StoreType;

public record UpdateStoreRequest(
        Long storeId,
        String storeName,
        String storeAddress,
        StoreType storeType
) {
}
