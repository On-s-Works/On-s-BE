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

    @Column(name = "base_address")
    private String baseAddress;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "is_active")
    boolean isActive = true;

    @Column(name = "is_sale")
    boolean isSale;

    @Column(name = "is_manage")
    boolean isManage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        this.isActive = true;
    }

    public void updateName(String storeName) {
        this.storeName = storeName;
    }

    public void updateAddress(String baseAddress, String addressDetail) {
        this.baseAddress = baseAddress;
        this.addressDetail = addressDetail;
    }

    public void updateStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public void delete() {
        this.isActive = false;
    }

    @Builder
    public Store(Long storeId, StoreType storeType, String storeName, String baseAddress, String addressDetail, boolean isActive, boolean isSale, boolean isManage, User user) {
        this.storeId = storeId;
        this.storeType = storeType;
        this.storeName = storeName;
        this.baseAddress = baseAddress;
        this.addressDetail = addressDetail;
        this.isActive = isActive;
        this.isSale = isSale;
        this.isManage = isManage;
        this.user = user;
    }
}
