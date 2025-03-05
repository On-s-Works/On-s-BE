package com.ons.back.persistence.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public void delete() {
        isActive = false;
    }

    @Builder
    public Item(Long id, String itemName, Double itemPrice, Integer itemStock, boolean isActive, Store store) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.isActive = isActive;
        this.store = store;
    }
}
