package com.ons.back.persistence.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "store_user")
public class StoreUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_user_id")
    private Long id;

    @Column(name = "store_user_name")
    private String storeUserName;

    @Column(name = "store_user_display_name")
    private String storeUserDisplayName;

    @Column(name = "registerDate")
    private LocalDate registerDate;

    @Column(name = "store_user_point")
    private Double storeUserPoint;

    @Column(name = "store_user_total_payment")
    private Double storeUserTotalPayment;

    @Column(name = "store_user_last_login")
    private LocalDate storeUserLastLogin;

    @Column(name = "store_user_type")
    private String storeUserType;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public void updateStoreUserName(String storeUserName) {
        this.storeUserName = storeUserName;
    }

    public void updateStoreUserDisplayName(String storeUserDisplayName) {
        this.storeUserDisplayName = storeUserDisplayName;
    }

    public void updateStoreUserType(String storeUserType) {
        this.storeUserType = storeUserType;
    }

    public void updateRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    @Builder
    public StoreUser(Long id, String storeUserName, String storeUserDisplayName, LocalDate registerDate, Double storeUserPoint, Double storeUserTotalPayment, LocalDate storeUserLastLogin, String storeUserType, Store store) {
        this.id = id;
        this.storeUserName = storeUserName;
        this.storeUserDisplayName = storeUserDisplayName;
        this.registerDate = registerDate;
        this.storeUserPoint = storeUserPoint;
        this.storeUserTotalPayment = storeUserTotalPayment;
        this.storeUserLastLogin = storeUserLastLogin;
        this.storeUserType = storeUserType;
        this.store = store;
    }
}
