package com.ons.back.presentation.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record ReadStoreUserAnalyticsResponse(
        Page<ReadStoreUserResponse> storeUserPage,
        long totalCount,
        long todayRegisterCount,
        double todayIncreaseRate,
        long weekRegisterCount,
        double weekIncreaseRate,
        long monthRegisterCount,
        double monthIncreaseRate
) {
}
