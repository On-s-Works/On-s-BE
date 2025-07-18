package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.EmailAuthentication;
import com.ons.back.persistence.domain.type.AuthType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


public record SendEmailRequest(

        @Email(message = "올바르지 않은 이메일 형식입니다.")
        @NotBlank
        String email

) {
    public EmailAuthentication toResetEntity(String authCode) {
        return EmailAuthentication.builder()
                .email(email)
                .authCode(authCode)
                .authType(AuthType.RESET)
                .isActive((byte)1)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
