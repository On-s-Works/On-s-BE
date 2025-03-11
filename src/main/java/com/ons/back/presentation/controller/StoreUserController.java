package com.ons.back.presentation.controller;

import com.ons.back.application.service.StoreUserService;
import com.ons.back.presentation.dto.response.ReadStoreUserAnalyticsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store-user")
@Tag(name = "가게 회원 API", description = "가게 회원 API")
public class StoreUserController {

    private final StoreUserService storeUserService;

    @Operation(summary = "가게 회원 데이터 조회", description = "가게 회원에 관련된 모든 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 회원 관리 조회 성공", content = @Content(schema = @Schema(implementation = ReadStoreUserAnalyticsResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<ReadStoreUserAnalyticsResponse> analytics(@AuthenticationPrincipal UserDetails user, @RequestParam Long storeId, Pageable pageable) {
        return ResponseEntity.ok(storeUserService.analytics(user.getUsername(), storeId, pageable));
    }
}
