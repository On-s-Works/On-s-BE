package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Order;
import com.ons.back.persistence.domain.PosDevice;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.OrderStatusType;
import com.ons.back.persistence.domain.type.PaymentType;
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
import java.util.Comparator;
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
    public List<ReadOrderResponse> getOrderList(
            String userKey, Long storeId, LocalDateTime start, LocalDateTime end, String sortType, List<String> paymentStatusList, List<String> paymentTypeList) {

        Store store = validateStoreOwner(userKey, storeId);

        List<Order> orderList = new ArrayList<>();
        List<PosDevice> posDeviceList = posDeviceRepository.findByStore(store);

        if(paymentStatusList != null && paymentTypeList != null) {
            for(PosDevice posDevice : posDeviceList) {
                for(String paymentType : paymentTypeList) {
                    orderList.addAll(orderRepository.findByPosDeviceAndCreatedAtBetweenAndPaymentType(posDevice,start.toLocalDate().atStartOfDay(), end.plusDays(1).toLocalDate().atStartOfDay(), PaymentType.valueOf(paymentType)));
                }

                for(String paymentStatus : paymentStatusList) {
                    orderList.addAll(orderRepository.findByPosDeviceAndCreatedAtBetweenAndOrderStatus(posDevice,start.toLocalDate().atStartOfDay(), end.plusDays(1).toLocalDate().atStartOfDay(), OrderStatusType.valueOf(paymentStatus)));
                }
            }
        } else {
            for(PosDevice posDevice : posDeviceList) {
                orderList.addAll(orderRepository.findByPosDeviceAndCreatedAtBetween(posDevice, start.toLocalDate().atStartOfDay(), end.plusDays(1).toLocalDate().atStartOfDay()));
            }
        }

        Comparator<Order> comparator = switch (sortType.toLowerCase()) {
            case "id_asc" -> Comparator.comparing(Order::getId);
            case "id_desc" -> Comparator.comparing(Order::getId).reversed();
            case "created_at_asc", "oldest" -> Comparator.comparing(Order::getCreatedAt);
            default -> Comparator.comparing(Order::getCreatedAt).reversed();
        };

        return orderList
                .stream()
                .sorted(comparator)
                .map(ReadOrderResponse::fromEntity)
                .toList();
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
