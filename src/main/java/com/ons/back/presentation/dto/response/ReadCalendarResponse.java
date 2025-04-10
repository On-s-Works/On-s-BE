package com.ons.back.presentation.dto.response;

import com.ons.back.persistence.domain.StoreCalendar;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadCalendarResponse(
        Long id,
        LocalDateTime start,
        LocalDateTime end,
        String content
) {
    public static ReadCalendarResponse fromEntity(StoreCalendar calendar) {
        return ReadCalendarResponse.builder()
                .id(calendar.getId())
                .start(calendar.getStart())
                .end(calendar.getEnd())
                .content(calendar.getContent())
                .build();
    }
}
