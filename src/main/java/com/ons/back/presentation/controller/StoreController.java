package com.ons.back.presentation.controller;

import com.ons.back.application.service.StoreService;
import com.ons.back.commons.dto.PrincipalDetails;
import com.ons.back.presentation.dto.request.CreateStoreRequest;
import com.ons.back.presentation.dto.request.UpdateStoreRequest;
import com.ons.back.presentation.dto.response.ReadStoreResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
@Tag(name = "가게 API", description = "가게 API")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "가게 전체 조회", description = "토큰을 사용하여 가게 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 전체 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<ReadStoreResponse>> getStores(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(storeService.getStoreByUserId(user.getUsername()));
    }

    @Operation(summary = "가게 상세 조회", description = "id를 사용하여 가게 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 상세 조회 성공", content = @Content(schema = @Schema(implementation = ReadStoreResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{storeId}")
    public ResponseEntity<ReadStoreResponse> getStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStoreById(storeId));
    }

    @Operation(summary = "가게 생성", description = "관리할 가게를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "가게 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<Void> createStore(@AuthenticationPrincipal UserDetails user, @RequestBody CreateStoreRequest request, @RequestParam(required = false) MultipartFile file) {
        storeService.createStore(user.getUsername(), request, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "가게 수정", description = "가게의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "가게 이름 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping
    public ResponseEntity<Void> updateStoreName(@AuthenticationPrincipal UserDetails user, @RequestBody UpdateStoreRequest request, @RequestParam(required = false) MultipartFile file) {
        storeService.updateStore(user.getUsername(), request, file);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "가게 삭제", description = "가게를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "가게 이름 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@AuthenticationPrincipal PrincipalDetails principal, @PathVariable Long storeId) {
        storeService.deleteStore(principal.getUsername(), storeId);
        return ResponseEntity.noContent().build();
    }
}
