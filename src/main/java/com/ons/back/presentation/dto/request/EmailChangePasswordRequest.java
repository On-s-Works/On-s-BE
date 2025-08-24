package com.ons.back.presentation.dto.request;

public record EmailChangePasswordRequest(
        String email,
        String password,
        String authCode
) {
}
