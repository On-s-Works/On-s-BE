package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.ProviderType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ReadUserResponse(
        String uid,
        String username,
        LocalDate birthday,
        String email,
        String phoneNumber,
        Boolean agreeTerms,
        ProviderType providerType
) {
    public static ReadUserResponse fromEntity(User user) {
        return ReadUserResponse.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .agreeTerms(user.getAgreeTerms())
                .providerType(user.getProviderType())
                .build();
    }
}
