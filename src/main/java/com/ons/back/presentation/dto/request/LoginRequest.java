package com.ons.back.presentation.dto.request;

public record LoginRequest(
        String uid,
        String password
) {
}
