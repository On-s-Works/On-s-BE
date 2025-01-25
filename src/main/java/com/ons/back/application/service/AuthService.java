package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.commons.utils.KeyGenerator;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.ProviderType;
import com.ons.back.persistence.domain.type.Role;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest request) {
        userRepository.findByUid(request.uid())
                .ifPresentOrElse(user -> {
                    throw new ApplicationException(
                            ErrorStatus.toErrorStatus("이미 존재하는 유저입니다.", 400, LocalDateTime.now())
                    );
                }, () -> {
                    userRepository.save(User.builder()
                            .uid(request.uid())
                            .password(passwordEncoder.encode(request.password()))
                            .providerType(ProviderType.LOCAL)
                            .userKey(KeyGenerator.generateKey())
                            .role(Role.USER)
                            .build());
                        });
    }
}
