package com.ons.back.presentation.dto.response;

import lombok.Builder;

@Builder
public record ReadSaleReportResponse(
        SaleReportResponse todaySaleReport,
        SaleReportResponse yesterdaySaleReport,
        SaleReportResponse lastWeekSaleReport,
        SaleReportResponse lastMonthSaleReport
) {
}
