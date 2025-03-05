package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.StoreType;

public record CreateStoreRequest(
        String storeName,
        String baseAddress,
        String addressDetail,
        StoreType storeType,
        String storeNumber,
        boolean isManage,
        boolean isSale
) {
    public Store toEntity(User user, String imageUrl) {
        return Store.builder()
                .user(user)
                .storeName(storeName)
                .baseAddress(baseAddress)
                .addressDetail(addressDetail)
                .storeType(storeType)
                .storeNumber(storeNumber)
                .isManage(isManage)
                .isSale(isSale)
                .storeImage(imageUrl)
                .build();
    }
}
