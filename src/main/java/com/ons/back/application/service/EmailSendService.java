package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmailSendService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String EMAIL_SENDER_ADDR;

    @Value("${spring.mail.subject}")
    private String EMAIL_SUBJECT;

    @Value("${spring.mail.sender.name}")
    private String EMAIL_SENDER_NAME;

    @Async
    public void sendEmail(String email, String authCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setFrom(EMAIL_SENDER_ADDR, EMAIL_SENDER_NAME); //메일 송신자
            mimeMessageHelper.setTo(email); //메일 수신자
            mimeMessageHelper.setSubject("[" + EMAIL_SUBJECT + "] 이메일 인증코드입니다."); //메일 제목
            mimeMessageHelper.setText(setContext(authCode), true);
            log.info("메일 발송에 성공하였습니다.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("메일 발송에 실패하였습니다.");
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("메일 발송에 실패하였습니다.", 500, LocalDateTime.now())
            );
        }

        javaMailSender.send(mimeMessage);
    }

    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("guideText", "On's 회원가입 인증코드입니다.");

        return templateEngine.process("join", context);
    }
}
