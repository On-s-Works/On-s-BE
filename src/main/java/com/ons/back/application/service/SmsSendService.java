package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.presentation.dto.request.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


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

    @Async
    public void sendSmsContent(SendMessageRequest request, String content, LocalDateTime reservationTime) {
        Message message = new Message();
        message.setFrom(FROM_NUMBER);
        message.setTo(request.to());
        message.setText(content);

        ZoneOffset zoneOffset = ZoneId.of("Asia/Seoul").getRules().getOffset(reservationTime);
        Instant instant = reservationTime.toInstant(zoneOffset);

        try {
            messageService.send(message, instant);
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException | NurigoUnknownException e) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("sms 발송 중 오류가 발생하였습니다.", 500, LocalDateTime.now())
            );
        }
    }
}
