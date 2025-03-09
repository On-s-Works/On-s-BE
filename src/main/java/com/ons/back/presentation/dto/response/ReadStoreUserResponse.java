package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.StoreUser;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ReadStoreUserResponse(
        Long id,
        String storeUserName,
        String storeUserDisplayName,
        LocalDate storeUserCreatedAt,
        Double storeUserPoint,
        Double storeUserTotalPayment,
        LocalDate storeUserLastLogin
) {
    public static ReadStoreUserResponse fromEntity(StoreUser storeUser) {
        return ReadStoreUserResponse.builder()
                .id(storeUser.getId())
                .storeUserName(storeUser.getStoreUserName())
                .storeUserDisplayName(storeUser.getStoreUserDisplayName())
                .storeUserCreatedAt(storeUser.getStoreUserCreatedAt())
                .storeUserTotalPayment(storeUser.getStoreUserTotalPayment())
                .storeUserLastLogin(storeUser.getStoreUserLastLogin())
                .build();
    }
}
