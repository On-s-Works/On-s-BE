package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Inquiry;
import com.ons.back.persistence.domain.type.StoreType;

public record CreateInquiryRequest(
        String username,
        String phoneNumber,
        String email,
        StoreType storeType,
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
