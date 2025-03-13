package com.ons.back.presentation.dto.response;

import lombok.Builder;

@Builder
public record SaleReportResponse(
        Integer saleCount,
        Double sumOfTotalAmount,
        Double increasePercent
) {
}
