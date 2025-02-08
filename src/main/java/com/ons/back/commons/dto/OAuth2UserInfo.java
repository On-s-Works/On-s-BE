package com.ons.back.commons.dto;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.commons.utils.KeyGenerator;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.ProviderType;
import com.ons.back.persistence.domain.type.Role;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String profile,
        ProviderType providerType
) {
    public static OAuth2UserInfo  of(String registrationId,  Map<String, Object> attributes) {
        return switch(registrationId) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new ApplicationException(ErrorStatus.toErrorStatus(
                    "해당하는 registrationId가 없습니다.", 404, LocalDateTime.now()
            ));
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .profile((String)attributes.get("picture"))
                .providerType(ProviderType.GOOGLE)
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .providerType(ProviderType.KAKAO)
                .build();
    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2UserInfo.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .profile(null)
                .providerType(ProviderType.NAVER)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .username(name)
                .email(email)
                .providerType(providerType)
                .userKey(KeyGenerator.generateKey())
                .role(Role.USER)
                .build();
    }
}
