package com.ons.back.commons.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    private final String frontRedirect;

    public CustomOAuth2FailureHandler(@Value("${frontend.redirect.uri}") String frontRedirect) {
        this.frontRedirect = frontRedirect;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,HttpServletResponse response, AuthenticationException exception) throws IOException {

        String redirectUrl = UriComponentsBuilder.fromUriString(frontRedirect)
                .queryParam("error", exception.getMessage())
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
