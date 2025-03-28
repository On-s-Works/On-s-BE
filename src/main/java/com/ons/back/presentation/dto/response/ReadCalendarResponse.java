package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.StoreCalendar;
import lombok.Builder;

@Builder
public record ReadCalendarResponse(
        Long id,
        int year,
        int month,
        int day,
        String content
) {
    public static ReadCalendarResponse fromEntity(StoreCalendar calendar) {
        return ReadCalendarResponse.builder()
                .id(calendar.getId())
                .year(calendar.getYear())
                .month(calendar.getMonth())
                .day(calendar.getDay())
                .content(calendar.getContent())
                .build();
    }
}
