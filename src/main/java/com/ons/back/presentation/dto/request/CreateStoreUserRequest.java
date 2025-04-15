package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.StoreUser;

import java.time.LocalDate;

public record CreateStoreUserRequest(
        Long storeId,
        String userName,
        String displayName,
        LocalDate registerDate,
        String storeUserType
) {
    public StoreUser toEntity(Store store) {
        return StoreUser.builder()
                .storeUserName(userName)
                .storeUserDisplayName(displayName)
                .registerDate(registerDate)
                .storeUserPoint(0d)
                .storeUserTotalPayment(0d)
                .storeUserLastLogin(LocalDate.now())
                .storeUserType(storeUserType)
                .store(store)
                .build();
    }
}