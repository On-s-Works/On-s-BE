package com.ons.back.presentation.controller;

import com.ons.back.application.service.UserService;
import com.ons.back.commons.dto.PrincipalDetails;
import com.ons.back.presentation.dto.request.UpdatePasswordRequest;
import com.ons.back.presentation.dto.request.UpdateSocialLoginUserRequest;
import com.ons.back.presentation.dto.response.ReadUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "유저 API", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 정보 조회", description = "토큰을 사용하여 유저 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 조회 성공", content = @Content(schema = @Schema(implementation = ReadUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<ReadUserResponse> getUser(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(userService.getUserByKey(user.getUsername()));
    }

    @Operation(summary = "유저의 비밀번호를 변경합니다.", description = "유저의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserDetails user, @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(user.getUsername(), request.password());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "소셜 로그인 이후 정보 업데이트 하는 api 입니다.", description = "소셜 로그인 이후 정보 업데이트 하는 api 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/password")
    public ResponseEntity<Void> updateSocialLoginUser(@AuthenticationPrincipal UserDetails user, @RequestBody UpdateSocialLoginUserRequest request) {
        userService.updateTermsAndPhoneNumber(user.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
