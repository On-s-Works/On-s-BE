package com.ons.back.presentation.controller;

import com.ons.back.application.service.StoreUserMemoService;
import com.ons.back.presentation.dto.request.CreateStoreUserMemoRequest;
import com.ons.back.presentation.dto.request.DeleteStoreUserMemoRequest;
import com.ons.back.presentation.dto.request.UpdateStoreUserMemoRequest;
import com.ons.back.presentation.dto.response.ReadStoreUserMemoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memo")
@Tag(name = "가게 회원 메모 API", description = "가게 회원 메모 API")
public class StoreUserMemoController {

    private final StoreUserMemoService storeUserMemoService;

    @Operation(summary = "가게 회원 데이터 조회", description = "가게 회원 메모들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ReadStoreUserMemoResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<ReadStoreUserMemoResponse>> getByStoreUserId(@RequestParam Long storeUserId) {
        return ResponseEntity.ok(storeUserMemoService.getByStoreUserId(storeUserId));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateStoreUserMemoRequest createStoreUserMemoRequest) {
        return ResponseEntity.ok(storeUserMemoService.create(createStoreUserMemoRequest));
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UpdateStoreUserMemoRequest updateStoreUserMemoRequest) {
        storeUserMemoService.update(updateStoreUserMemoRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody DeleteStoreUserMemoRequest deleteStoreUserMemoRequest) {
        storeUserMemoService.delete(deleteStoreUserMemoRequest);
        return ResponseEntity.noContent().build();
    }
}
