package com.ons.back.presentation.dto.request;

import java.time.LocalDate;

public record UpdateStoreUserRequest(
        String storeUserName,
        String displayName,
        LocalDate registerDate,
        String storeUserType,
        Long storeId,
        Long storeUserId
) {
}
