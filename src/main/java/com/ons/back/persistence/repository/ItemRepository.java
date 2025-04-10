package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.Item;
import com.ons.back.persistence.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStore_StoreId(Long storeId);
    List<Item> findTop4ByStoreOrderByItemStockAsc(Store store);
}
