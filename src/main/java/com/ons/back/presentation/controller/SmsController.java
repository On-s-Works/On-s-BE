package com.ons.back.presentation.controller;

import com.ons.back.application.service.SmsAuthenticationService;
import com.ons.back.presentation.dto.request.SMSAuthRequest;
import com.ons.back.presentation.dto.request.SendMessageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sms")
@Tag(name = "문자 API", description = "문자 API")
public class SmsController {

    private final SmsAuthenticationService smsAuthenticationService;

    @Operation(summary = "인증 문자을 발송합니다.", description = "인증 문자을 발송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문자 발송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody SendMessageRequest request) {
        smsAuthenticationService.sendSMSAuth(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "문자을 검증합니다.", description = "문자을 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문자 발송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/check")
    public ResponseEntity<Void> checkMessage(@RequestBody SMSAuthRequest request) {
        smsAuthenticationService.checkSMSAuthCode(request);
        return ResponseEntity.noContent().build();
    }
}
