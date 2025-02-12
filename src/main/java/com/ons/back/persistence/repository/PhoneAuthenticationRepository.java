package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.PhoneAuthentication;
import com.ons.back.persistence.domain.type.AuthType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PhoneAuthenticationRepository extends JpaRepository<PhoneAuthentication, Long> {
    List<PhoneAuthentication> findByPhoneNumberAndAuthType(String email, AuthType authType);
    Optional<PhoneAuthentication> findByPhoneNumberAndAuthCodeAndIsActiveAndCreatedAtAfter(String email, String authCode, byte isActive, LocalDateTime createdAt);
}
