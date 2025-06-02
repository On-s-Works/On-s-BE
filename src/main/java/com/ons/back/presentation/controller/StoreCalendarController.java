package com.ons.back.presentation.controller;

import com.ons.back.application.service.StoreCalendarService;
import com.ons.back.presentation.dto.request.CreateStoreCalendarRequest;
import com.ons.back.presentation.dto.response.ReadCalendarResponse;
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
@RequestMapping("/store-calendar")
@Tag(name = "가게 일정 API", description = "가게 일정 API")
public class StoreCalendarController {

    private final StoreCalendarService storeCalendarService;

    @Operation(summary = "가게 일정 조회", description = "년도, 달을 받아 가게의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<ReadCalendarResponse>> getCalendar(@RequestParam Long storeId, @RequestParam Integer year, @RequestParam Integer month, @RequestParam Integer day) {
        return ResponseEntity.ok(storeCalendarService.getStoreCalendarByYearAndMonth(storeId, year, month, day));
    }

    @Operation(summary = "가게 일정 생성", description = "가게의 일정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "가게 일정 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<ReadCalendarResponse> createCalendar(@RequestBody CreateStoreCalendarRequest request) {
        return ResponseEntity.ok(storeCalendarService.createStoreCalendar(request));
    }
}
