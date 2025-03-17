package com.ons.back.presentation.dto.response;

import lombok.Builder;

@Builder
public record ReadSaleChangeResponse(
        Double firstMonthSaleAmount,
        Double secondMonthSaleAmount,
        Double thirdMonthSaleAmount,
        Double forthMonthSaleAmount,
        Double fifthMonthSaleAmount,
        Double sixthMonthSaleAmount,
        Double totalAmount,
        Double avg
) {
}
