package com.ons.back.presentation.controller;

import com.ons.back.application.service.SmsAuthenticationService;
import com.ons.back.presentation.dto.request.SMSAuthRequest;
import com.ons.back.presentation.dto.request.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsAuthenticationService smsAuthenticationService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody SendMessageRequest request) {
        smsAuthenticationService.sendSMSAuth(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> checkMessage(@RequestBody SMSAuthRequest request) {
        smsAuthenticationService.checkSMSAuthCode(request);
        return ResponseEntity.noContent().build();
    }
}
