package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(String accessToken);
    void deleteByAccessToken(String accessToken);
}
