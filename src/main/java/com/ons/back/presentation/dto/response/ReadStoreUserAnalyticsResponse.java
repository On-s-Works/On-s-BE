package com.ons.back.presentation.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReadStoreUserAnalyticsResponse(
        List<ReadStoreUserResponse> storeUserList,
        long totalCount,
        long todayRegisterCount,
        double todayIncreaseRate,
        long weekRegisterCount,
        double weekIncreaseRate,
        long monthRegisterCount,
        double monthIncreaseRate
) {
}
