package com.ons.back.presentation.controller;

import com.ons.back.application.service.StoreUserService;
import com.ons.back.presentation.dto.request.CreateStoreUserMessageRequest;
import com.ons.back.presentation.dto.request.CreateStoreUserRequest;
import com.ons.back.presentation.dto.response.ReadStoreUserAnalyticsResponse;
import com.ons.back.presentation.dto.response.ReadStoreUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    @GetMapping("/analytics")
    public ResponseEntity<ReadStoreUserAnalyticsResponse> analytics(@AuthenticationPrincipal UserDetails user, @RequestParam Long storeId) {
        return ResponseEntity.ok(storeUserService.analytics(user.getUsername(), storeId));
    }

    @GetMapping
    public ResponseEntity<Page<ReadStoreUserResponse>> getStoreUser(@AuthenticationPrincipal UserDetails user,
                                                                    @RequestParam Long storeId,
                                                                    @RequestParam(required = false, defaultValue = "created_at_desc") String sortType,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startTime,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endTime,
                                                                    @RequestParam(required = false) List<String> userTypeList,
                                                                    Pageable pageable) {
        if (startTime == null) {
            startTime = LocalDate.now();
        }

        if (endTime == null) {
            endTime = LocalDate.now().plusDays(1);
        }

        return ResponseEntity.ok(storeUserService.getStoreUser(user.getUsername(), storeId, sortType, startTime, endTime, userTypeList, pageable));
    }

    @PostMapping
    public ResponseEntity<Long> createStoreUser(@AuthenticationPrincipal UserDetails user, @RequestBody CreateStoreUserRequest request) {
        return ResponseEntity.ok(storeUserService.createStoreUser(user.getUsername(), request));
    }

    @PostMapping("/message")
    public ResponseEntity<Void> sendMessage(@AuthenticationPrincipal UserDetails user, @RequestBody CreateStoreUserMessageRequest request) {
        storeUserService.sendStoreUserMessage(user.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
