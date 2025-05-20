package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StoreUserRepository extends JpaRepository<StoreUser, Long> {
    List<StoreUser> findByStore(Store store);
    List<StoreUser> findByRegisterDateBetweenAndStore(LocalDate startDate, LocalDate endDate, Store store);
    List<StoreUser> findByRegisterDateBetweenAndStoreUserTypeAndStore(LocalDate startDate, LocalDate endDate, String storeUserType, Store store);
}
