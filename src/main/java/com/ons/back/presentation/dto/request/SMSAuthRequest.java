package com.ons.back.presentation.dto.request;

public record SMSAuthRequest(
        String phoneNumber,
        String authCode
) {
}
