package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckResponse(
        String uid,
        LocalDateTime createdAt
) {
    public static CheckResponse fromUser(User user) {
        return CheckResponse.builder()
                .uid(user.getUid())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
