package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByIsAnsweredFalse();
}
