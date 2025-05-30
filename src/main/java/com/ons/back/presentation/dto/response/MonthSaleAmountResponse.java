package com.ons.back.presentation.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MonthSaleAmountResponse(
        Double amount,
        LocalDateTime dateTime
) {
}
