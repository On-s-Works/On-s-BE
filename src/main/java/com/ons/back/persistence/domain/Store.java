package com.ons.back.persistence.domain;

import com.ons.back.persistence.domain.type.StoreType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "store")
@SQLRestriction("is_active = true")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_type")
    private StoreType storeType;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_address")
    private String storeAddress;

    @Column(name = "is_active")
    boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void updateName(String storeName) {
        this.storeName = storeName;
    }

    public void updateAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public void updateStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public void delete() {
        this.isActive = false;
    }

    @Builder
    public Store(Long storeId, StoreType storeType, String storeName, String storeAddress, Boolean isActive, User user) {
        this.storeId = storeId;
        this.storeType = storeType;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.isActive = isActive;
        this.user = user;
    }
}
