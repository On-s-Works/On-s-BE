package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.StoreUserMemo;
import lombok.Builder;

@Builder
public record ReadStoreUserMemoResponse(
        Long storeUserId,
        Long memoId,
        String content
) {
    public static ReadStoreUserMemoResponse fromEntity(StoreUserMemo entity) {
        return ReadStoreUserMemoResponse.builder()
                .storeUserId(entity.getStoreUser().getId())
                .memoId(entity.getId())
                .content(entity.getContent())
                .build();
    }
}
