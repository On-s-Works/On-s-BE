package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.Item;
import com.ons.back.persistence.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStore_StoreIdAndIsActiveTrue(Long storeId);
    List<Item> findTop4ByStoreAndIsActiveTrueOrderByItemStockAsc(Store store);
    List<Item> findTop4ByStoreAndIsActiveTrueOrderByItemStockAscAndIsOrderedFalse(Store store);
    List<Item> findByStore_StoreIdAndItemStockAndIsActiveTrue(Long storeId, Integer itemStock);
    List<Item> findByStore_StoreIdAndIsActiveTrueAndItemStockGreaterThan(Long storeId, Integer stock);
    List<Item> findByStore_StoreIdAndIsOrderedAndIsActiveTrue(Long storeId, Boolean isOrdered);
}
