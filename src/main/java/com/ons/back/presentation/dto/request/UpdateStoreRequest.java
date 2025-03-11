package com.ons.back.presentation.dto.request;

public record UpdateStoreRequest(
        Long storeId,
        String storeName,
        String baseAddress,
        String addressDetail,
        String storeNumber,
        Boolean isManage,
        Boolean isSale,
        String storeType
) {
}
