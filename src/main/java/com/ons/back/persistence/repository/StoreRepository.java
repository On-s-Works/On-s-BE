package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByUser_UserId(Long userId);
    void deleteByUser(User user);
}
