package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.StoreCalendar;

import java.time.LocalDateTime;

public record CreateStoreCalendarRequest(
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long storeId,
        String content
) {
    public StoreCalendar toEntity(Store store) {
        return StoreCalendar.builder()
                .store(store)
                .start(startDate)
                .end(endDate)
                .content(content)
                .build();
    }
}
