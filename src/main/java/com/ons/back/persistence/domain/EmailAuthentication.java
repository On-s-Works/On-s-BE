package com.ons.back.persistence.domain;

import com.ons.back.persistence.domain.type.MailAuthType;
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
@Table(name = "email_authentication")
public class EmailAuthentication {

    @Id
    @Column(name = "email_authentication_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private MailAuthType authType;

    @Column(name = "auth_code", length = 6, columnDefinition = "CHAR(6)")
    private String authCode;

    @Column(name = "is_active")
    private Byte isActive;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public void updateIsActive(Byte isActive) {this.isActive = isActive;}

    @Builder
    public EmailAuthentication(Long id, String email, MailAuthType authType, String authCode, Byte isActive, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.authType = authType;
        this.authCode = authCode;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
}
