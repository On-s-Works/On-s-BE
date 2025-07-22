package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.StoreUser;
import com.ons.back.persistence.domain.StoreUserMemo;

public record CreateStoreUserMemoRequest(
        Long storeUserId,
        String content
) {
    public StoreUserMemo toEntity(StoreUser storeUser) {
        return StoreUserMemo.builder()
                .storeUser(storeUser)
                .content(content)
                .build();
    }
}
