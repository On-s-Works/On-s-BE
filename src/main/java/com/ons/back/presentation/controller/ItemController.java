package com.ons.back.presentation.controller;

import com.ons.back.application.service.ItemService;
import com.ons.back.presentation.dto.request.CreateItemRequest;
import com.ons.back.presentation.dto.request.DeleteItemRequest;
import com.ons.back.presentation.dto.request.UpdateItemRequest;
import com.ons.back.presentation.dto.response.ReadItemResponse;
import com.ons.back.presentation.dto.response.ReadLowStockItemResponse;
import com.ons.back.presentation.dto.response.ReadSaledItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "아이템 생성", description = "가게 아이템 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addItem(@AuthenticationPrincipal UserDetails user,
                                        @RequestPart(value = "request") CreateItemRequest request,
                                        @RequestPart(value = "file", required = false) MultipartFile file) {
        itemService.createItem(user.getUsername(), request, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "제품 판매 현황 조회", description = "제품 판매 현황을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "제품 판매 현황 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/saled")
    public ResponseEntity<List<ReadSaledItemResponse>> getSaledItems(@AuthenticationPrincipal UserDetails user,
                                                                     @RequestParam LocalDateTime start,
                                                                     @RequestParam LocalDateTime end,
                                                                     @RequestParam Long storeId) {
        return ResponseEntity.ok(itemService.getSaledItem(start, end, storeId, user.getUsername()));
    }

    @Operation(summary = "제품 관리", description = "제품 관리.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "제품 관리 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<ReadItemResponse>> getItems(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam Long storeId,
            @RequestParam(required = false, defaultValue = "created_at_desc") String sortType,
            @RequestParam(required = false) Boolean isOrdered,
            @RequestParam(required = false) Boolean saleStatus
    ) {
        return ResponseEntity.ok(itemService.getItemsByStoreId(user.getUsername(), storeId, sortType, isOrdered, saleStatus));
    }

    @Operation(summary = "재고 임박 상품 조회", description = "재고 임박 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재고 임박 상품 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/low-stock")
    public ResponseEntity<List<ReadLowStockItemResponse>> getLowStockItems(@AuthenticationPrincipal UserDetails user, @RequestParam Long storeId) {
        return ResponseEntity.ok(itemService.getLowStockItem(user.getUsername(), storeId));
    }

    @Operation(summary = "재고 임박 상품 (주문 안된 것) 조회", description = "재고 임박 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재고 임박 상품 조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/low-stock-false")
    public ResponseEntity<List<ReadLowStockItemResponse>> getLowStockItemsFalse(@AuthenticationPrincipal UserDetails user, @RequestParam Long storeId) {
        return ResponseEntity.ok(itemService.getLowStockItemFalse(user.getUsername(), storeId));
    }

    @Operation(summary = "제품 삭제", description = "제품 삭제.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "제품 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteItem(@RequestBody DeleteItemRequest request, @AuthenticationPrincipal UserDetails user) {
        itemService.deleteItem(user.getUsername(), request.itemId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "제품 수정", description = "제품 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "제품 수정"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateItem(@RequestPart(value = "request") UpdateItemRequest request,
                                           @RequestPart(required = false, value = "file") MultipartFile file,
                                           @AuthenticationPrincipal UserDetails user) {
        itemService.updateItem(user.getUsername(), request, file);
        return ResponseEntity.noContent().build();
    }
}
