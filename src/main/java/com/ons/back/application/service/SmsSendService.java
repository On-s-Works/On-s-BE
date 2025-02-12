package com.ons.back.application.service;

import com.ons.back.presentation.dto.request.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional
public class SmsSendService {

    private final DefaultMessageService messageService;

    @Value("${sms.fromnumber}")
    private String FROM_NUMBER;

    @Async
    public void sendSms(SendMessageRequest request, String authCode) {
        Message message = new Message();
        message.setFrom(FROM_NUMBER);
        message.setTo(request.to());
        message.setText("On's 본인확인 인증번호는 " + authCode + "입니다.");

        messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
