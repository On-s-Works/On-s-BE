package com.ons.back.persistence.domain;

import com.ons.back.persistence.domain.type.PaymentStatusType;
import com.ons.back.persistence.domain.type.PaymentType;
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
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentType paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatusType paymentStatus;

    @Column(name = "payment_time")
    LocalDateTime paymentTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public Payment(Long id, PaymentType paymentMethod, PaymentStatusType paymentStatus, LocalDateTime paymentTime, Order order) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentTime = paymentTime;
        this.order = order;
    }
}
