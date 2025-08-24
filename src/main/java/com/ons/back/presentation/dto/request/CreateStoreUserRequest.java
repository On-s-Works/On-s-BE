package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.StoreUser;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CreateStoreUserRequest(
        Long storeId,
        String userName,
        String displayName,
        @PastOrPresent(message = "가입일은 오늘날짜 이전이거나 오늘이여야 합니다.")
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