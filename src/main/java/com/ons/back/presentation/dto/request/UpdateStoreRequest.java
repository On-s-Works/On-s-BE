package com.ons.back.presentation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateStoreRequest(
        Long storeId,
        String storeName,
        String baseAddress,
        String addressDetail,
        String storeNumber,
        Boolean isManage,
        Boolean isSale,
        String storeType,
        LocalDate createdDate,
        LocalTime openTime
) {
}
