package com.ons.back.presentation.controller;

import com.ons.back.application.service.OrderService;
import com.ons.back.presentation.dto.response.ReadOrderDetailResponse;
import com.ons.back.presentation.dto.response.ReadOrderResponse;
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

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Tag(name = "주문 API", description = "주문 API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 상세 조회", description = "주문 상세사항을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 상세 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{storeId}")
    public ResponseEntity<List<ReadOrderResponse>> getOrderDetail(@AuthenticationPrincipal UserDetails user, @PathVariable Long storeId) {
        return ResponseEntity.ok(orderService.getOrderList(user.getUsername(), storeId));
    }

    @Operation(summary = "주문 상세 조회", description = "주문 상세사항을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 상세 조회 성공", content = @Content(schema = @Schema(implementation = ReadOrderDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<ReadOrderDetailResponse> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }
}
