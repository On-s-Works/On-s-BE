package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.Order;
import com.ons.back.persistence.domain.PosDevice;
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
    List<Order> findByPosDeviceAndCreatedAtBetween(PosDevice posDevices, LocalDateTime start, LocalDateTime end);
}
