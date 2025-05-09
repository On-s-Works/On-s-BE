package com.ons.back.presentation.controller;

import com.ons.back.application.service.AuthService;
import com.ons.back.commons.dto.TokenResponse;
import com.ons.back.presentation.dto.request.LoginRequest;
import com.ons.back.presentation.dto.request.SignUpRequest;
import com.ons.back.presentation.dto.response.ReadUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "유저 API", description = "유저 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "일반 회원 가입", description = "일반 유저 회원 가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃", description = "엑세스 토큰을 제거합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails user, HttpServletResponse response) {
        authService.logout(user.getUsername(), response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "uid 중복 확인", description = "true시 중복 x / false시 중복 o")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "uid 중복확인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkDuplicatedUid(@RequestParam String uid) {
        return ResponseEntity.ok(authService.checkDuplicatedUid(uid));
    }
}
