package com.ons.back.presentation.dto.request;

import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.StoreCalendar;

public record CreateStoreCalendarRequest(
        Long storeId,
        Integer year,
        Integer month,
        Integer day,
        String content
) {
    public StoreCalendar toEntity(Store store) {
        return StoreCalendar.builder()
                .store(store)
                .year(year)
                .month(month)
                .day(day)
                .content(content)
                .build();
    }
}
