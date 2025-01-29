package com.ons.back.presentation.controller;

import com.ons.back.application.service.StoreService;
import com.ons.back.commons.dto.PrincipalDetails;
import com.ons.back.persistence.domain.type.StoreType;
import com.ons.back.presentation.dto.request.CreateStoreRequest;
import com.ons.back.presentation.dto.request.UpdateStoreRequest;
import com.ons.back.presentation.dto.response.ReadStoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<List<ReadStoreResponse>> getStores(@AuthenticationPrincipal PrincipalDetails principal) {
        return ResponseEntity.ok(storeService.getStoreByUserId(principal.getUsername()));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<ReadStoreResponse> getStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStoreById(storeId));
    }

    @PostMapping
    public ResponseEntity<Void> createStore(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody CreateStoreRequest request) {
        storeService.createStore(principal.getUsername(), request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/storeName")
    public ResponseEntity<Void> updateStoreName(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody UpdateStoreRequest request) {
        storeService.updateStoreName(principal.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/storeAddress")
    public ResponseEntity<Void> updateStoreAddress(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody UpdateStoreRequest request) {
        storeService.updateStoreAddress(principal.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/storeType")
    public ResponseEntity<Void> updateStoreType(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody UpdateStoreRequest request) {
        storeService.updateStoreType(principal.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/storeType")
    public ResponseEntity<List<StoreType>> getStoreTypes() {
        return ResponseEntity.ok(storeService.getStoreType());
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@AuthenticationPrincipal PrincipalDetails principal, @PathVariable Long storeId) {
        storeService.deleteStore(principal.getUsername(), storeId);
        return ResponseEntity.noContent().build();
    }
}
