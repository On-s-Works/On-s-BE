package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Inquiry;

public record CreateInquiryRequest(
        String username,
        String phoneNumber,
        String email,
        String storeType,
        String content,
        boolean agreePrivacy
) {
    public Inquiry toEntity() {
        return Inquiry.builder()
                .name(username)
                .phoneNumber(phoneNumber)
                .email(email)
                .storeType(storeType)
                .content(content)
                .agreePrivacy(agreePrivacy)
                .build();
    }
}
