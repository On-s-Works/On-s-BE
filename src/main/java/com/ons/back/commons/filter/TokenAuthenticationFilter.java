package com.ons.back.commons.filter;

import com.ons.back.commons.dto.TokenResponse;
import com.ons.back.commons.utils.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    public static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        if(shouldSkipFilter(uri)){
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveToken(request);
        String refreshToken = resolveRefreshToken(request);

        if (tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else if(tokenProvider.validateToken(refreshToken)) {
            TokenResponse reissuedToken = tokenProvider.reissueAccessToken(accessToken, refreshToken);

            if(StringUtils.hasText(reissuedToken.accessToken()) && StringUtils.hasText(reissuedToken.refreshToken())){
                setAuthentication(reissuedToken.accessToken());
                response.setHeader(AUTHORIZATION, TOKEN_PREFIX + reissuedToken.accessToken());
                setRefreshTokenCookie(response, reissuedToken.refreshToken());
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipFilter(String uri) {
        AntPathMatcher pathMatcher = new AntPathMatcher();

        List<String> skippedUris = Arrays.asList(
                "/favicon.ico",
                "/error",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/oauth2/**",
                "/login/oauth2/**",
                "/auth/**",
                "/login"
        );

        for (String skippedUri : skippedUris) {
            if (pathMatcher.match(skippedUri, uri)) {
                return true;
            }
        }

        return false;
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        return token.substring(TOKEN_PREFIX.length());
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh_token")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }
}
