package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Store;
import lombok.Builder;

@Builder
public record ReadStoreResponse(
        Long id,
        String storeName,
        String baseAddress,
        String addressDetail,
        String storeType,
        boolean isSale,
        boolean isManage,
        String storeImage,
        String storeNumber
) {
    public static ReadStoreResponse fromEntity(Store store) {
        return ReadStoreResponse.builder()
                .id(store.getStoreId())
                .storeName(store.getStoreName())
                .baseAddress(store.getBaseAddress())
                .addressDetail(store.getAddressDetail())
                .storeType(store.getStoreType())
                .isSale(store.isSale())
                .isManage(store.isManage())
                .storeImage(store.getStoreImage())
                .storeNumber(store.getStoreNumber())
                .build();
    }
}
