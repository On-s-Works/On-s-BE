package com.ons.back.presentation.dto.request;

public record UpdateStoreRequest(
        Long storeId,
        String storeName,
        String baseAddress,
        String addressDetail,
        String storeType
) {
}
