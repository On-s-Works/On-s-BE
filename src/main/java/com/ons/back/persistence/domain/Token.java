package com.ons.back.persistence.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "jwt", timeToLive = 60 * 60 * 24)
public class Token {

    @Id
    private String id;

    @Indexed
    private String accessToken;

    private String refreshToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Builder
    public Token(String id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
