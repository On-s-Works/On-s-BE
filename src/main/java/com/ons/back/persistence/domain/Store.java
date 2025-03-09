package com.ons.back.persistence.domain;

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
    private String storeType;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "base_address")
    private String baseAddress;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "store_image")
    private String storeImage;

    @Column(name = "store_number")
    private String storeNumber;

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

    public void updateStoreType(String storeType) {
        this.storeType = storeType;
    }

    public void updateNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public void updateIsManage(Boolean isManage) {
        this.isManage = isManage;
    }

    public void updateIsSale(Boolean isSale) {
        this.isSale = isSale;
    }

    public void delete() {
        this.isActive = false;
    }

    public void updateImage(String imageUrl) {
        this.storeImage = imageUrl;
    }

    @Builder
    public Store(Long storeId, String storeType, String storeName, String baseAddress, String addressDetail, String storeImage, String storeNumber, boolean isActive, boolean isSale, boolean isManage, User user) {
        this.storeId = storeId;
        this.storeType = storeType;
        this.storeName = storeName;
        this.baseAddress = baseAddress;
        this.addressDetail = addressDetail;
        this.storeImage = storeImage;
        this.storeNumber = storeNumber;
        this.isActive = isActive;
        this.isSale = isSale;
        this.isManage = isManage;
        this.user = user;
    }
}
