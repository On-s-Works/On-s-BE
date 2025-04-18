package com.ons.back.application.service;

import com.ons.back.commons.dto.OAuth2UserInfo;
import com.ons.back.commons.dto.PrincipalDetails;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

        User user = userRepository.findByEmailAndProviderType(oAuth2UserInfo.email(), oAuth2UserInfo.providerType())
                .orElseGet(() -> userRepository.save(oAuth2UserInfo.toEntity()));

        return new PrincipalDetails(user, oAuth2UserAttributes, userNameAttributeName);
    }
}
