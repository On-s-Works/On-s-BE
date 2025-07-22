package com.ons.back.presentation.dto.request;

public record UpdateStoreUserMemoRequest(
        Long id,
        String content
) {
}
