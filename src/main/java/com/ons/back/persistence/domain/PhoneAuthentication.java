package com.ons.back.persistence.domain;

import com.ons.back.persistence.domain.type.AuthType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "phone_authentication")
public class PhoneAuthentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_authentication_id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthType authType;

    @Column(name = "auth_code", length = 6, columnDefinition = "CHAR(6)")
    private String authCode;

    @Column(name = "is_active")
    private Byte isActive;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public void updateIsActive(Byte isActive) {this.isActive = isActive;}

    @Builder
    public PhoneAuthentication(Long id, String phoneNumber, AuthType authType, String authCode, Byte isActive, LocalDateTime createdAt) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.authType = authType;
        this.authCode = authCode;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
}
