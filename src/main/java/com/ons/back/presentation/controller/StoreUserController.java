package com.ons.back.presentation.controller;

import com.ons.back.application.service.StoreUserService;
import com.ons.back.presentation.dto.request.CreateStoreUserMessageRequest;
import com.ons.back.presentation.dto.request.CreateStoreUserRequest;
import com.ons.back.presentation.dto.request.DeleteStoreUserRequest;
import com.ons.back.presentation.dto.request.UpdateStoreUserRequest;
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

    @Operation(summary = "가게의 사용자들을 가져옵니다.", description = "가게 사용자들을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 사용자 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<Page<ReadStoreUserResponse>> getStoreUser(@AuthenticationPrincipal UserDetails user,
                                                                    @RequestParam Long storeId,
                                                                    @RequestParam(required = false, defaultValue = "register_date_desc") String sortType,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startTime,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endTime,
                                                                    @RequestParam(required = false) List<String> userTypeList,
                                                                    @RequestParam(required = false) String searchKeyword,
                                                                    Pageable pageable) {
        if (startTime == null) {
            startTime = LocalDate.now();
        }

        if (endTime == null) {
            endTime = LocalDate.now().plusDays(1);
        }

        return ResponseEntity.ok(storeUserService.getStoreUser(user.getUsername(), storeId, sortType, startTime, endTime, userTypeList, pageable, searchKeyword));
    }

    @Operation(summary = "가게 사용자 생성", description = "가게 사용자 생성.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 사용자 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<Long> createStoreUser(@AuthenticationPrincipal UserDetails user, @RequestBody CreateStoreUserRequest request) {
        return ResponseEntity.ok(storeUserService.createStoreUser(user.getUsername(), request));
    }

    @Operation(summary = "가게 사용자 메시지 보내기", description = "가게 사용자에게 메시지 보내기 (현재 번호, 이메일 받는 값 없어서 작동 x)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 보내기 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/message")
    public ResponseEntity<Void> sendMessage(@AuthenticationPrincipal UserDetails user, @RequestBody CreateStoreUserMessageRequest request) {
        storeUserService.sendStoreUserMessage(user.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "가게 사용자 수정", description = "가게 사용자 수정.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 사용자 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping
    public ResponseEntity<Void> updateStoreUser(@AuthenticationPrincipal UserDetails user, @RequestBody UpdateStoreUserRequest request) {
        storeUserService.updateStoreUser(user.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStoreUser(@AuthenticationPrincipal UserDetails user, @RequestBody DeleteStoreUserRequest request) {
        storeUserService.deleteStoreUser(user.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
