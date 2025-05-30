package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Order;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.repository.OrderRepository;
import com.ons.back.persistence.repository.StoreRepository;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.request.CreateStoreRequest;
import com.ons.back.presentation.dto.request.UpdateStoreRequest;
import com.ons.back.presentation.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<ReadStoreResponse> getStoreByUserId(String userKey) {

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        return storeRepository.findByUser_UserId(user.getUserId()).stream().map(ReadStoreResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public ReadStoreResponse getStoreById(Long storeId) {
        return ReadStoreResponse.fromEntity(storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                )));
    }

    public void createStore(String userKey, CreateStoreRequest request, MultipartFile file) {

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        String imageUrl = null;

        if(file != null && !file.isEmpty()) {
            imageUrl = storageService.uploadImage(file);
        }

        storeRepository.save(request.toEntity(user, imageUrl));
    }

    public void updateStore(String userKey, UpdateStoreRequest request, MultipartFile file) {
        Store store = validateStoreOwner(userKey, request.storeId());

        if(request.storeName() != null) {
            store.updateName(request.storeName());
        }

        if(request.storeNumber() != null) {
            store.updateNumber(request.storeNumber());
        }

        if(request.isManage() != store.isManage()) {
            store.updateIsManage(request.isManage());
        }

        if(request.isSale() != store.isSale()) {
            store.updateIsSale(request.isSale());
        }

        if(request.storeType() != null) {
            store.updateStoreType(request.storeType());
        }

        if(request.baseAddress() != null && request.addressDetail() != null) {
            store.updateAddress(request.baseAddress(), request.addressDetail());
        }

        if(file != null && !file.isEmpty()) {
            storageService.deleteImage(store.getStoreImage());
            store.updateImage(storageService.uploadImage(file));
        }
    }

    public void deleteStore(String userKey, Long storeId) {
        Store store = validateStoreOwner(userKey, storeId);
        store.delete();
    }

    public ReadSaleReportResponse getSalesReport(String userKey, Long storeId) {

        Store store = validateStoreOwner(userKey, storeId);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);
        LocalDateTime yesterdayStart = todayStart.minusDays(1);
        LocalDateTime lastWeekStart = todayStart.minusDays(7);
        LocalDateTime lastMonthStart = todayStart.minusMonths(1);


        List<Order> todayOrderList = new ArrayList<>();
        List<Order> yesterdayOrderList = new ArrayList<>();
        List<Order> lastWeekOrderList = new ArrayList<>();
        List<Order> lastMonthOrderList = new ArrayList<>();

        todayOrderList.addAll(orderRepository.findByStoreAndCreatedAtBetween(store, todayStart, todayEnd));
        yesterdayOrderList.addAll(orderRepository.findByStoreAndCreatedAtBetween(store, yesterdayStart, todayStart));
        lastWeekOrderList.addAll(orderRepository.findByStoreAndCreatedAtBetween(store, lastWeekStart, todayStart));
        lastMonthOrderList.addAll(orderRepository.findByStoreAndCreatedAtBetween(store, lastMonthStart, todayStart));

        Double todayTotalAmount = 0d;
        Double yesterdayTotalAmount = 0d;
        Double lastWeekTotalAmount = 0d;
        Double lastMonthTotalAmount = 0d;

        for(Order order : todayOrderList) {
            todayTotalAmount += order.getTotalAmount();
        }

        for(Order order : yesterdayOrderList) {
            yesterdayTotalAmount += order.getTotalAmount();
        }

        for(Order order : lastWeekOrderList) {
            lastWeekTotalAmount += order.getTotalAmount();
        }

        for(Order order : lastMonthOrderList) {
            lastMonthTotalAmount += order.getTotalAmount();
        }

        SaleReportResponse todaySaleReport = toSaleReportResponse(todayOrderList, todayTotalAmount, yesterdayTotalAmount);
        SaleReportResponse yesterdaySaleReport = toSaleReportResponse(yesterdayOrderList, yesterdayTotalAmount, todayTotalAmount);
        SaleReportResponse lastWeekSaleReport = toSaleReportResponse(lastWeekOrderList, lastWeekTotalAmount, todayTotalAmount);
        SaleReportResponse lastMonthSaleReport = toSaleReportResponse(lastMonthOrderList, lastMonthTotalAmount, todayTotalAmount);

        return ReadSaleReportResponse.builder()
                .todaySaleReport(todaySaleReport)
                .yesterdaySaleReport(yesterdaySaleReport)
                .lastWeekSaleReport(lastWeekSaleReport)
                .lastMonthSaleReport(lastMonthSaleReport)
                .build();
    }

    public ReadSaleChangeResponse getSaleChange(String userKey, Long storeId) {

        Store store = validateStoreOwner(userKey, storeId);

        LocalDateTime now = LocalDateTime.now();
        List<LocalDateTime> dates = new ArrayList<>();

        for (int i = 7; i >= 0; i--) {
            LocalDateTime firstDay = now.minusMonths(i)
                    .withDayOfMonth(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
            dates.add(firstDay);
        }

        List<Double> monthSaleAmounts = new ArrayList<>();

        for(int i = 0; i < 6; i++) {
            monthSaleAmounts.add(orderRepository.findByStoreAndCreatedAtBetween(store, dates.get(i), dates.get(i + 1))
                    .stream().map(Order::getTotalAmount)
                    .reduce(0d, Double::sum));
        }

        Double total = monthSaleAmounts.stream().mapToDouble(Double::doubleValue).sum();

        List<MonthSaleAmountResponse> monthSaleAmountList = new ArrayList<>();

        for(int i = 0; i < 6; i++) {
            monthSaleAmountList.add(MonthSaleAmountResponse.builder()
                    .dateTime(dates.get(i))
                    .amount(monthSaleAmounts.get(i))
                    .build());
        }

        return ReadSaleChangeResponse.builder()
                .monthSaleAmountList(monthSaleAmountList)
                .totalAmount(total)
                .avg(total/6)
                .build();
    }

    private static SaleReportResponse toSaleReportResponse(List<Order> orderList, Double firstTotalAmount, Double secondTotalAmount) {
        return SaleReportResponse.builder()
                .saleCount(orderList.size())
                .sumOfTotalAmount(firstTotalAmount)
                .increasePercent(secondTotalAmount != 0 ? (firstTotalAmount - secondTotalAmount) / secondTotalAmount * 100 : 100)
                .build();
    }

    private Store validateStoreOwner(String userKey, Long storeId) {
        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        if(!user.equals(store.getUser())) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("가게의 주인이 아닙니다.", 404, LocalDateTime.now())
            );
        }
        return store;
    }
}
