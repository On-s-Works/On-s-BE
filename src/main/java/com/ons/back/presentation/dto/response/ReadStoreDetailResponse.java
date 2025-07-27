package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Store;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ReadStoreDetailResponse(
        Long id,
        String storeName,
        String baseAddress,
        String addressDetail,
        String storeType,
        boolean isSale,
        boolean isManage,
        String storeImage,
        String storeNumber,
        LocalTime openTime,
        LocalDate createdDate
) {
    public static ReadStoreDetailResponse fromEntity(Store store) {
        return ReadStoreDetailResponse.builder()
                .id(store.getStoreId())
                .storeName(store.getStoreName())
                .baseAddress(store.getBaseAddress())
                .addressDetail(store.getAddressDetail())
                .storeType(store.getStoreType())
                .isSale(store.isSale())
                .isManage(store.isManage())
                .storeImage(store.getStoreImage())
                .storeNumber(store.getStoreNumber())
                .openTime(store.getOpenTime())
                .createdDate(store.getCreatedDate())
                .build();
    }
}
