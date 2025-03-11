package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.StoreUser;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.repository.StoreRepository;
import com.ons.back.persistence.repository.StoreUserRepository;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.response.ReadStoreUserAnalyticsResponse;
import com.ons.back.presentation.dto.response.ReadStoreUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class StoreUserService {

    private final StoreUserRepository storeUserRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public ReadStoreUserAnalyticsResponse analytics(String userKey, Long storeId, Pageable pageable) {

        LocalDateTime now = LocalDateTime.now();

        Store store = storeRepository.findById(storeId)
                        .orElseThrow(() -> new ApplicationException(
                                ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                        ));

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        if(!store.getUser().equals(user)) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("해당 가게의 주인이 아닙니다.", 400, LocalDateTime.now())
            );
        }

        List<ReadStoreUserResponse> storeUserResponseList = storeUserRepository.findByStore(store).stream().map(ReadStoreUserResponse::fromEntity).toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), storeUserResponseList.size());

        List<ReadStoreUserResponse> subList = storeUserResponseList.subList(start, end);
        Page<ReadStoreUserResponse> page = new PageImpl<>(subList, pageable, storeUserResponseList.size());

        long size = storeUserResponseList.size();

        long todayRegisterStoreUser = storeUserResponseList.stream()
                .filter(
                        storeUser -> storeUser.storeUserCreatedAt().equals(now.toLocalDate())
                ).count();

        long yesterdayRegisterStoreUser = storeUserResponseList.stream()
                .filter(
                        storeUser -> storeUser.storeUserCreatedAt().equals(now.toLocalDate().minusDays(1))
                ).count();

        long thisWeekRegisterStoreUser = storeUserResponseList.stream()
                .filter(storeUser -> storeUser.storeUserCreatedAt().isAfter(LocalDate.now().minusDays(7)))
                .count();

        long lastWeekRegisterStoreUser = storeUserResponseList.stream()
                .filter(storeUser -> storeUser.storeUserCreatedAt().isAfter(LocalDate.now().minusDays(14)))
                .count() - thisWeekRegisterStoreUser;

        long thisMonthRegisterStoreUser = storeUserResponseList.stream()
                .filter(storeUser -> storeUser.storeUserCreatedAt().getYear() == now.getYear() &&
                        storeUser.storeUserCreatedAt().getMonth() == now.getMonth())
                .count();

        long lastMonthRegisterStoreUser = storeUserResponseList.stream()
                .filter(storeUser -> {
                    LocalDate createdAt = storeUser.storeUserCreatedAt();
                    int createdYear = createdAt.getYear();
                    Month createdMonth = createdAt.getMonth();

                    int lastMonthYear = (now.getMonth() == Month.JANUARY) ? now.getYear() - 1 : now.getYear();
                    Month lastMonth = now.getMonth().minus(1);

                    return createdYear == lastMonthYear && createdMonth == lastMonth;
                })
                .count();

        return ReadStoreUserAnalyticsResponse.builder()
                .totalCount(size)
                .storeUserPage(page)
                .todayRegisterCount(todayRegisterStoreUser)
                .todayIncreaseRate(yesterdayRegisterStoreUser == 0 ? 100.0 : (double)todayRegisterStoreUser / yesterdayRegisterStoreUser)
                .weekRegisterCount(thisWeekRegisterStoreUser)
                .weekIncreaseRate(lastWeekRegisterStoreUser == 0 ? 100.0 : (double)thisWeekRegisterStoreUser / lastWeekRegisterStoreUser)
                .monthRegisterCount(thisMonthRegisterStoreUser)
                .monthIncreaseRate(lastMonthRegisterStoreUser == 0 ? 100.0 : (double)thisMonthRegisterStoreUser / lastMonthRegisterStoreUser)
                .build();
    }
}
