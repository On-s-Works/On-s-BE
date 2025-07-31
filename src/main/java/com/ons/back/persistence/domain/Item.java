package com.ons.back.persistence.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_price")
    private Double itemPrice;

    @Column(name = "item_stock")
    private Integer itemStock;

    @Column(name = "barcode", length = 13)
    private String barcode;

    @Column(name = "item_image")
    private String itemImage;

    @Column(name = "item_purchase_price")
    private Double itemPurchasePrice;

    @Column(name = "is_ordered")
    private Boolean isOrdered;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_sale")
    private Boolean isSale;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public void delete() {
        isActive = false;
    }

    public void updateItemStock(Integer itemStock) {
        this.itemStock = itemStock;
    }

    public void updateIsOrdered(Boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public void updateItemPurchasePrice(Double itemPurchasePrice) {
        this.itemPurchasePrice = itemPurchasePrice;
    }

    public void updateItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void updateBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Builder
    public Item(Long id, String itemName, Double itemPrice, Integer itemStock, String barcode, String itemImage, Double itemPurchasePrice, Boolean isOrdered, boolean isActive, Boolean isSale, LocalDateTime createdAt, Store store) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.barcode = barcode;
        this.itemImage = itemImage;
        this.itemPurchasePrice = itemPurchasePrice;
        this.isOrdered = isOrdered;
        this.isActive = isActive;
        this.isSale = isSale;
        this.createdAt = createdAt;
        this.store = store;
    }
}
