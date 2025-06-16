package com.ons.back.presentation.dto.request;

public record UpdateSocialLoginUserRequest(
        String phoneNumber,
        Boolean agreeTerms
) {
}
