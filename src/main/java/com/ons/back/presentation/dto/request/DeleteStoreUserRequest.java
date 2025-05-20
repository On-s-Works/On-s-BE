package com.ons.back.presentation.dto.request;

public record DeleteStoreUserRequest(
        Long storeId,
        Long storeUserId
) {
}
