package com.ons.back.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmailAuthRequest(
        @Email(message = "올바르지 않은 이메일 형식입니다.")
        @NotNull(message = "올바르지 않은 이메일 형식입니다.")
        @NotBlank(message = "올바르지 않은 이메일 형식입니다.")
        String email,

        @NotBlank(message = "인증 코드는 6자 입니다.")
        @NotNull(message = "인증 코드는 6자 입니다.")
        @Size(max = 6, min = 6, message = "인증 코드는 6자 입니다.")
        String authCode
) {
}
