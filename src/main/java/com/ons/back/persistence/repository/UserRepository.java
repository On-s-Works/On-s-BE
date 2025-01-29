package com.ons.back.persistence.repository;

import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProviderType(String email, ProviderType providerType);
    Optional<User> findByUid(String uid);
    Optional<User> findByUserKey(String key);
}
