package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.EmailAuthentication;
import com.ons.back.persistence.domain.type.AuthType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record SendEmailRequest(

        @Email(message = "올바르지 않은 이메일 형식입니다.")
        @NotBlank
        String email

) {
    public EmailAuthentication toJoinEntity(String authCode) {
        return EmailAuthentication.builder()
                .email(email)
                .authCode(authCode)
                .authType(AuthType.JOIN)
                .isActive((byte)1)
                .build();
    }
}
