package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.PhoneAuthentication;
import com.ons.back.persistence.domain.type.AuthType;

import java.time.LocalDateTime;

public record SendMessageRequest(
        String to
) {
    public PhoneAuthentication toJoinEntity(String authCode) {
        return PhoneAuthentication.builder()
                .phoneNumber(to)
                .authCode(authCode)
                .authType(AuthType.JOIN)
                .isActive((byte)1)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
