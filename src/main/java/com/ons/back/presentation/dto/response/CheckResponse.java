package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.ProviderType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckResponse(
        String uid,
        LocalDateTime createdAt,
        ProviderType providerType
) {
    public static CheckResponse fromUser(User user) {
        return CheckResponse.builder()
                .uid(user.getUid())
                .createdAt(user.getCreatedAt())
                .providerType(user.getProviderType())
                .build();
    }
}
