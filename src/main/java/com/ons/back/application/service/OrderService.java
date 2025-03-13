package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Order;
import com.ons.back.persistence.domain.PosDevice;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.repository.OrderRepository;
import com.ons.back.persistence.repository.PosDeviceRepository;
import com.ons.back.persistence.repository.StoreRepository;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.response.ReadOrderDetailResponse;
import com.ons.back.presentation.dto.response.ReadOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final PosDeviceRepository posDeviceRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public ReadOrderDetailResponse getOrderDetail(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 주문이 없습니다.", 404, LocalDateTime.now())
                ));

        return ReadOrderDetailResponse.fromEntity(order);
    }

    @Transactional(readOnly = true)
    public List<ReadOrderResponse> getOrderList(String userKey, Long storeId) {

        Store store = validateStoreOwner(userKey, storeId);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        List<Order> orderList = new ArrayList<>();
        List<PosDevice> posDeviceList = posDeviceRepository.findByStore(store);

        for(PosDevice posDevice : posDeviceList) {
            orderList.addAll(orderRepository.findByPosDeviceAndCreatedAtBetween(posDevice, todayStart, todayEnd));
        }

        return orderList.stream().map(ReadOrderResponse::fromEntity).toList();
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
