package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.Inquiry;
import lombok.Builder;

@Builder
public record ReadUnAnsweredInquiryResponse(
        Long id,
        String title
) {
    public static ReadUnAnsweredInquiryResponse fromEntity(Inquiry inquiry) {
        return ReadUnAnsweredInquiryResponse.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .build();
    }
}
