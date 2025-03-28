package com.ons.back.presentation.controller;

import com.ons.back.application.service.StoreCalendarService;
import com.ons.back.presentation.dto.request.CreateStoreCalendarRequest;
import com.ons.back.presentation.dto.response.ReadCalendarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store-calendar")
public class StoreCalendarController {

    private final StoreCalendarService storeCalendarService;

    @GetMapping
    public ResponseEntity<List<ReadCalendarResponse>> getCalendar(@RequestParam Long storeId, @RequestParam Integer year, @RequestParam Integer month) {
        return ResponseEntity.ok(storeCalendarService.getStoreCalendarByYearAndMonth(storeId, year, month));
    }

    @PostMapping
    public ResponseEntity<ReadCalendarResponse> createCalendar(@RequestBody CreateStoreCalendarRequest request) {
        return ResponseEntity.ok(storeCalendarService.createStoreCalendar(request));
    }
}
