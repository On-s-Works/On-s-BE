package com.ons.back.persistence.domain;

import com.ons.back.persistence.domain.type.ProviderType;
import com.ons.back.persistence.domain.type.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Table(name = "user")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "uid")
    private String uid;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "agree_terms")
    private Boolean agreeTerms;

    @Column(name = "user_key", unique = true)
    private String userKey;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long userId, String uid, String password, String username, LocalDate birthday, String email, String phoneNumber, Boolean agreeTerms, String userKey, ProviderType providerType, Role role) {
        this.userId = userId;
        this.uid = uid;
        this.password = password;
        this.username = username;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.agreeTerms = agreeTerms;
        this.userKey = userKey;
        this.providerType = providerType;
        this.role = role;
    }
}
