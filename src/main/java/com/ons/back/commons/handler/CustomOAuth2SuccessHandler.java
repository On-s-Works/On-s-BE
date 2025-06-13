package com.ons.back.commons.handler;

import com.ons.back.commons.utils.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final String frontRedirect;

    @Autowired
    public CustomOAuth2SuccessHandler(TokenProvider tokenProvider, @Value("${frontend.redirect.uri}") String frontRedirect) {
        this.tokenProvider = tokenProvider;
        this.frontRedirect = frontRedirect;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication, accessToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        String redirectUri = UriComponentsBuilder.fromUriString(frontRedirect)
                .queryParam("accessToken", accessToken)
                .build().toUriString();

        response.sendRedirect(redirectUri);
    }
}
