package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.PosDevice;
import com.ons.back.persistence.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PosDeviceRepository extends JpaRepository<PosDevice, Long> {
    List<PosDevice> findByStore(Store store);
}
