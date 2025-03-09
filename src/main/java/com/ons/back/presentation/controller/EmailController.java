package com.ons.back.presentation.controller;

import com.ons.back.application.service.EmailAuthenticationService;
import com.ons.back.commons.exception.ValidationFailedException;
import com.ons.back.presentation.dto.request.EmailAuthRequest;
import com.ons.back.presentation.dto.request.SendEmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@Tag(name = "이메일 API", description = "이메일 API")
public class EmailController {

    private final EmailAuthenticationService emailAuthenticationService;

    @Operation(summary = "인증 이메일을 발송합니다.", description = "인증 이메일을 발송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 발송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid SendEmailRequest request, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        emailAuthenticationService.sendEmailAuth(request);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "이메일을 검증합니다.", description = "이메일을 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 발송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/check")
    public ResponseEntity<Void> checkCode(@RequestBody @Valid EmailAuthRequest request, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        emailAuthenticationService.checkEmailAuthCode(request);

        return ResponseEntity.noContent().build();
    }
}
