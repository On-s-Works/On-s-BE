package com.ons.back.presentation.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReadSaleChangeResponse(
        List<MonthSaleAmountResponse> monthSaleAmountList,
        Double totalAmount,
        Double avg
) {
}
