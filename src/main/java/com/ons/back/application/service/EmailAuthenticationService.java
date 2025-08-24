package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.EmailAuthentication;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.AuthType;
import com.ons.back.persistence.repository.EmailAuthenticationRepository;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.request.EmailAuthRequest;
import com.ons.back.presentation.dto.request.EmailChangePasswordRequest;
import com.ons.back.presentation.dto.request.SendEmailRequest;
import com.ons.back.presentation.dto.response.CheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Transactional
public class EmailAuthenticationService {

    private final EmailAuthenticationRepository emailAuthenticationRepository;
    private final EmailSendService emailSendService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void sendEmailAuth(SendEmailRequest request){

        String authCode = createCode();

        emailSendService.sendEmail(request.email(), authCode);

        List<EmailAuthentication> emailAuthenticationList = emailAuthenticationRepository.findByEmailAndAuthType(request.email(), AuthType.RESET);
        emailAuthenticationList.forEach(emailAuthentication -> emailAuthentication.updateIsActive((byte)0));

        emailAuthenticationRepository.save(request.toResetEntity(authCode));
    }

    public CheckResponse checkEmailAuthCode(EmailAuthRequest request) {

        EmailAuthentication emailAuthentication = emailAuthenticationRepository.findByEmailAndAuthCodeAndIsActiveAndCreatedAtAfter(request.email(), request.authCode(), (byte)1, LocalDateTime.now().minusMinutes(10))
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("일치하지 않는 인증 코드입니다.", 400, LocalDateTime.now())
                ));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        return CheckResponse.fromUser(user);
    }

    public void emailAuthAndChangePassword(EmailChangePasswordRequest request){

        EmailAuthentication emailAuthentication = emailAuthenticationRepository.findByEmailAndAuthCodeAndIsActiveAndCreatedAtAfter(request.email(), request.authCode(), (byte)1, LocalDateTime.now().minusMinutes(10))
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("일치하지 않는 인증 코드입니다.", 400, LocalDateTime.now())
                ));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        user.updatePassword(passwordEncoder.encode(request.password()));
        emailAuthenticationRepository.deleteById(emailAuthentication.getId());
    }

    public String createCode() {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
