package com.ons.back.persistence.domain;

import com.ons.back.persistence.domain.type.PaymentStatusType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatusType paymentStatus;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public Payment(Long id, String paymentMethod, PaymentStatusType paymentStatus, LocalDateTime paymentTime, Order order) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentTime = paymentTime;
        this.order = order;
    }
}
