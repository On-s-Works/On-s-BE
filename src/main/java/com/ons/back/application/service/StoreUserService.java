package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Item;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.StoreUser;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.repository.StoreRepository;
import com.ons.back.persistence.repository.StoreUserRepository;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.request.CreateStoreUserMessageRequest;
import com.ons.back.presentation.dto.request.CreateStoreUserRequest;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class StoreUserService {

    private final StoreUserRepository storeUserRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final SmsSendService smsSendService;
    private final EmailSendService emailSendService;

    public ReadStoreUserAnalyticsResponse analytics(String userKey, Long storeId) {

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

        long size = storeUserResponseList.size();

        long todayRegisterStoreUser = storeUserResponseList.stream()
                .filter(
                        storeUser -> storeUser.registerDate().equals(now.toLocalDate())
                ).count();

        long yesterdayRegisterStoreUser = storeUserResponseList.stream()
                .filter(
                        storeUser -> storeUser.registerDate().equals(now.toLocalDate().minusDays(1))
                ).count();

        long thisWeekRegisterStoreUser = storeUserResponseList.stream()
                .filter(storeUser -> storeUser.registerDate().isAfter(LocalDate.now().minusDays(7)))
                .count();

        long lastWeekRegisterStoreUser = storeUserResponseList.stream()
                .filter(storeUser -> storeUser.registerDate().isAfter(LocalDate.now().minusDays(14)))
                .count() - thisWeekRegisterStoreUser;

        long thisMonthRegisterStoreUser = storeUserResponseList.stream()
                .filter(storeUser -> storeUser.registerDate().getYear() == now.getYear() &&
                        storeUser.registerDate().getMonth() == now.getMonth())
                .count();

        long lastMonthRegisterStoreUser = storeUserResponseList.stream()
                .filter(storeUser -> {
                    LocalDate createdAt = storeUser.registerDate();
                    int createdYear = createdAt.getYear();
                    Month createdMonth = createdAt.getMonth();

                    int lastMonthYear = (now.getMonth() == Month.JANUARY) ? now.getYear() - 1 : now.getYear();
                    Month lastMonth = now.getMonth().minus(1);

                    return createdYear == lastMonthYear && createdMonth == lastMonth;
                })
                .count();

        return ReadStoreUserAnalyticsResponse.builder()
                .totalCount(size)
                .todayRegisterCount(todayRegisterStoreUser)
                .todayIncreaseRate(yesterdayRegisterStoreUser == 0 ? 100.0 : (double)todayRegisterStoreUser / yesterdayRegisterStoreUser)
                .weekRegisterCount(thisWeekRegisterStoreUser)
                .weekIncreaseRate(lastWeekRegisterStoreUser == 0 ? 100.0 : (double)thisWeekRegisterStoreUser / lastWeekRegisterStoreUser)
                .monthRegisterCount(thisMonthRegisterStoreUser)
                .monthIncreaseRate(lastMonthRegisterStoreUser == 0 ? 100.0 : (double)thisMonthRegisterStoreUser / lastMonthRegisterStoreUser)
                .build();
    }

    public Long createStoreUser(String userKey, CreateStoreUserRequest request) {

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 고객이 없습니다.", 404, LocalDateTime.now())
                ));

        if(!store.getUser().equals(user)) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("권한이 없습니다.", 401, LocalDateTime.now())
            );
        }

        return storeUserRepository.save(request.toEntity(store)).getId();
    }

    public Page<ReadStoreUserResponse> getStoreUser(String userKey, Long storeId, String sortType, LocalDate startTime, LocalDate endTime, List<String> userTypeList, Pageable pageable) {

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

        List<StoreUser> storeUserList = new ArrayList<>();

        if(userTypeList == null || userTypeList.isEmpty()) {
            storeUserList.addAll(storeUserRepository.findByRegisterDateBetweenAndStore(startTime, endTime, store));
        } else {
            for(String type : userTypeList) {
                storeUserList.addAll(storeUserRepository.findByRegisterDateBetweenAndStoreUserTypeAndStore(startTime, endTime, type, store));
            }
        }

        Comparator<StoreUser> comparator = switch (sortType.toLowerCase()) {
            case "register_date_asc" -> Comparator.comparing(StoreUser::getRegisterDate);
            case "total_payment_desc" -> Comparator.comparing(StoreUser::getStoreUserTotalPayment);
            case "total_payment_asc" -> Comparator.comparing(StoreUser::getStoreUserTotalPayment).reversed();
            case "store_user_name_desc" -> Comparator.comparing(StoreUser::getStoreUserName);
            case "store_user_name_asc" -> Comparator.comparing(StoreUser::getStoreUserName).reversed();
            case "store_user_display_name_desc" -> Comparator.comparing(StoreUser::getStoreUserDisplayName);
            case "store_user_display_name_asc" -> Comparator.comparing(StoreUser::getStoreUserDisplayName).reversed();
            default -> Comparator.comparing(StoreUser::getRegisterDate).reversed();
        };

        storeUserList.sort(comparator);
        List<ReadStoreUserResponse> result = storeUserList.stream().map(ReadStoreUserResponse::fromEntity).toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), result.size());

        if (start >= result.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, result.size());
        }

        List<ReadStoreUserResponse> subList = result.subList(start, end);
        return new PageImpl<>(subList, pageable, result.size());
    }

    public void sendStoreUserMessage(String userKey, CreateStoreUserMessageRequest request) {

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        if(!store.getUser().equals(user)) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("권한이 없습니다.", 401, LocalDateTime.now())
            );
        }

        List<StoreUser> storeUserList = new ArrayList<>();

        for(Long id : request.storeUserIdList()) {
            storeUserList.add(storeUserRepository.findById(id)
                    .orElseThrow(() -> new ApplicationException(
                            ErrorStatus.toErrorStatus("해당하는 가게 유저가 없습니다.", 404, LocalDateTime.now())
                    )));

            //전화번호 없어서 못보냄
        }

//        smsSendService.sendSmsContent();
    }
}
