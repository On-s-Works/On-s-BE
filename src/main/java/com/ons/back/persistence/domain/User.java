package com.ons.back.persistence.domain;

import com.ons.back.persistence.domain.type.ProviderType;
import com.ons.back.persistence.domain.type.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "user")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "uid", unique = true)
    private String uid;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "agree_terms")
    private Boolean agreeTerms;

    @Column(name = "user_key", unique = true)
    private String userKey;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateAgreeTerms(Boolean agreeTerms) {
        this.agreeTerms = agreeTerms;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Builder
    public User(Long userId, String uid, String password, String username, String email, String phoneNumber, Boolean agreeTerms, String userKey, LocalDateTime createdAt, ProviderType providerType, Role role) {
        this.userId = userId;
        this.uid = uid;
        this.password = password;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.agreeTerms = agreeTerms;
        this.userKey = userKey;
        this.createdAt = createdAt;
        this.providerType = providerType;
        this.role = role;
    }
}
