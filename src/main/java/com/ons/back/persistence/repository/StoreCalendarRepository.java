package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.StoreCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StoreCalendarRepository extends JpaRepository<StoreCalendar, Long> {
    List<StoreCalendar> findByStore_StoreIdAndStartGreaterThanEqualAndStartLessThanEqual(
            Long storeId, LocalDateTime monthStart, LocalDateTime monthEnd);
}
