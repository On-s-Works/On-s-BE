package com.ons.back.presentation.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record CreateStoreUserMessageRequest(
        Long storeId,
        List<Long> storeUserIdList,
        LocalDateTime reservationTime,
        String content,
        List<String> sendType,
        String messageType
) {
}
