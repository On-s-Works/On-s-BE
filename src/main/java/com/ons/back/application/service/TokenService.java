package com.ons.back.application.service;

import com.ons.back.commons.exception.TokenException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Token;
import com.ons.back.persistence.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

    private final TokenRepository tokenRepository;

    public String saveOrUpdate(String userKey, String refreshToken, String accessToken) {

        Token token = tokenRepository.findByAccessToken(accessToken)
                .orElseGet(() -> tokenRepository.save(Token.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .userKey(userKey)
                        .build()));

        token.updateRefreshToken(refreshToken);

        return refreshToken;
    }

    public Token findByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(
                        ErrorStatus.toErrorStatus("토큰이 만료되었습니다", 401, LocalDateTime.now())));
    }

    @Transactional
    public void updateToken(String accessToken, Token token) {
        token.updateAccessToken(accessToken);
    }
}
