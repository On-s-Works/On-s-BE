package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Order;
import com.ons.back.persistence.repository.OrderRepository;
import com.ons.back.presentation.dto.response.ReadOrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public ReadOrderDetailResponse getOrderDetail(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 주문이 없습니다.", 404, LocalDateTime.now())
                ));

        return ReadOrderDetailResponse.fromEntity(order);
    }
}
