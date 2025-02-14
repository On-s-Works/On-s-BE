package com.ons.back.presentation.controller;

import com.ons.back.application.service.InquiryService;
import com.ons.back.presentation.dto.request.AnswerInquiryRequest;
import com.ons.back.presentation.dto.request.CreateInquiryRequest;
import com.ons.back.presentation.dto.response.ReadUnAnsweredInquiryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
@Tag(name = "문의 사항 API", description = "문의 사항 API")
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "응답되지 않은 문의 사항 조회", description = "응답되지 않은 문의 사항 조회.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<ReadUnAnsweredInquiryResponse>> getUnAnsweredInquiry() {
        return ResponseEntity.ok(inquiryService.getInquiryUnAnswered());
    }

    @Operation(summary = "문의 사항 생성", description = "문의 사항 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<Void> createInquiry(@RequestBody CreateInquiryRequest request) {
        inquiryService.createInquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "문의 사항 답변", description = "문의 사항 답변")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping
    public ResponseEntity<Void> answerInquiry(@RequestBody AnswerInquiryRequest request) {
        inquiryService.answerInquiry(request);
        return ResponseEntity.noContent().build();
    }
}
