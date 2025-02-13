package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.type.StoreType;
import lombok.Builder;

@Builder
public record ReadStoreResponse(
        Long id,
        String storeName,
        String baseAddress,
        String addressDetail,
        StoreType storeType
) {
    public static ReadStoreResponse fromEntity(Store store) {
        return ReadStoreResponse.builder()
                .id(store.getStoreId())
                .storeName(store.getStoreName())
                .baseAddress(store.getBaseAddress())
                .addressDetail(store.getAddressDetail())
                .storeType(store.getStoreType())
                .build();
    }
}
