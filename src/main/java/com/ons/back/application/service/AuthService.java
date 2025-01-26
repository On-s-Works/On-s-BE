package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Token;
import com.ons.back.persistence.repository.TokenRepository;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.request.SignUpRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest request) {
        userRepository.findByUid(request.uid())
                .ifPresentOrElse(user -> {
                    throw new ApplicationException(
                            ErrorStatus.toErrorStatus("이미 존재하는 유저입니다.", 400, LocalDateTime.now())
                    );
                }, () -> {
                    userRepository.save(request.toEntity(passwordEncoder));
                });
    }

    public void logout(String accessToken, HttpServletResponse response) {
        tokenRepository.deleteByAccessToken(accessToken);
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setHeader("Authorization", "");
    }
}
