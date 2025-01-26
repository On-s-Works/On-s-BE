package com.ons.back.presentation.dto.request;

import com.ons.back.commons.utils.KeyGenerator;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.ProviderType;
import com.ons.back.persistence.domain.type.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

public record SignUpRequest(
        String uid,
        String password,
        String username,
        LocalDate birthday,
        String phoneNumber,
        String email,
        boolean agreeTerms
) {
    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .uid(uid)
                .password(passwordEncoder.encode(password))
                .providerType(ProviderType.LOCAL)
                .userKey(KeyGenerator.generateKey())
                .role(Role.USER)
                .email(email)
                .agreeTerms(agreeTerms)
                .birthday(birthday)
                .phoneNumber(phoneNumber)
                .username(username)
                .build();
    }
}
