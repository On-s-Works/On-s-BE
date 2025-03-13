package com.ons.back.presentation.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReadSaleReportResponse(
        SaleReportResponse todaySaleReport,
        SaleReportResponse yesterdaySaleReport,
        SaleReportResponse lastWeekSaleReport,
        SaleReportResponse lastMonthSaleReport,
        List<ReadOrderResponse> todayOrderResponseList
) {
}
