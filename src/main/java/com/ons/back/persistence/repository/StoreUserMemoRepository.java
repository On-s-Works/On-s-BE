package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.StoreUserMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreUserMemoRepository extends JpaRepository<StoreUserMemo, Long> {
    List<StoreUserMemo> findByStoreUser_Id(Long storeUserId);
}
