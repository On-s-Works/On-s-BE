package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.EmailAuthentication;
import com.ons.back.persistence.domain.type.AuthType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailAuthenticationRepository extends JpaRepository<EmailAuthentication, Long> {
    List<EmailAuthentication> findByEmailAndAuthType(String email, AuthType authType);
    Optional<EmailAuthentication> findByEmailAndAuthCodeAndIsActiveAndCreatedAtAfter(String email, String authCode, byte isActive, LocalDateTime createdAt);
}
