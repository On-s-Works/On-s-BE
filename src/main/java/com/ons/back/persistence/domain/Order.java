package com.ons.back.persistence.domain;

import com.ons.back.persistence.domain.type.OrderStatusType;
import com.ons.back.persistence.domain.type.PaymentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Order.withOrderItems",
                attributeNodes = {
                        @NamedAttributeNode("orderItemList")
                }
        )
})
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Builder
    public Order(Long id, Double totalAmount, OrderStatusType orderStatus, PaymentType paymentType, LocalDateTime createdAt, Store store, List<OrderItem> orderItemList) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.paymentType = paymentType;
        this.createdAt = createdAt;
        this.store = store;
        this.orderItemList = orderItemList;
    }
}
