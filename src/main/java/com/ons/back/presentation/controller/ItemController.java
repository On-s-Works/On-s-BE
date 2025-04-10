package com.ons.back.presentation.controller;

import com.ons.back.application.service.ItemService;
import com.ons.back.presentation.dto.request.CreateItemRequest;
import com.ons.back.presentation.dto.request.UpdateItemRequest;
import com.ons.back.presentation.dto.response.ReadLowStockItemResponse;
import com.ons.back.presentation.dto.response.ReadSaledItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Void> addItem(@RequestBody CreateItemRequest request, @AuthenticationPrincipal UserDetails user) {
        itemService.createItem(user.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/saled")
    public ResponseEntity<List<ReadSaledItemResponse>> getSaledItems(@AuthenticationPrincipal UserDetails user,
                                                                     @RequestParam LocalDateTime start,
                                                                     @RequestParam LocalDateTime end,
                                                                     @RequestParam Long storeId) {
        return ResponseEntity.ok(itemService.getSaledItem(start, end, storeId, user.getUsername()));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ReadLowStockItemResponse>> getLowStockItems(@AuthenticationPrincipal UserDetails user, @RequestParam Long storeId) {
        return ResponseEntity.ok(itemService.getLowStockItem(user.getUsername(), storeId));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails user) {
        itemService.deleteItem(user.getUsername(), itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateItem(@RequestBody UpdateItemRequest request, @AuthenticationPrincipal UserDetails user) {
        itemService.updateItem(user.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
