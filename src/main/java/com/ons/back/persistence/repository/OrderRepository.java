package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.Order;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.type.OrderStatusType;
import com.ons.back.persistence.domain.type.PaymentType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(value = "Order.withOrderItems")
    @Override
    Optional<Order> findById(Long orderId);

    @EntityGraph(value = "Order.withOrderItems")
    List<Order> findByStoreAndCreatedAtBetween(Store store, LocalDateTime start, LocalDateTime end);

    @EntityGraph(value = "Order.withOrderItems")
    List<Order> findByStoreAndCreatedAtBetweenAndOrderStatus(Store store, LocalDateTime start, LocalDateTime end, OrderStatusType orderStatus);

    @EntityGraph(value = "Order.withOrderItems")
    List<Order> findByStoreAndCreatedAtBetweenAndPaymentType(Store store, LocalDateTime start, LocalDateTime end, PaymentType paymentType);
}
