package com.ons.back.presentation.controller;

import com.ons.back.application.service.AuthService;
import com.ons.back.commons.dto.TokenResponse;
import com.ons.back.presentation.dto.request.LoginRequest;
import com.ons.back.presentation.dto.request.SignUpRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken, HttpServletResponse response) {
        authService.logout(accessToken, response);
        return ResponseEntity.ok().build();
    }

}
