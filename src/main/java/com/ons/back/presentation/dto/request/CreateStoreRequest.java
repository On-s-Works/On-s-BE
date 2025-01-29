package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.StoreType;

public record CreateStoreRequest(
        String storeName,
        String storeAddress,
        StoreType storeType
) {
    public Store toEntity(User user) {
        return Store.builder()
                .user(user)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .storeType(storeType)
                .build();
    }
}
