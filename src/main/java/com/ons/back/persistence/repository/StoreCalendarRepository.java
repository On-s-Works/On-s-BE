package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.StoreCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreCalendarRepository extends JpaRepository<StoreCalendar, Long> {
    List<StoreCalendar> findByStore_StoreIdAndYearAndMonth(Long storeId, int year, int month);
}
