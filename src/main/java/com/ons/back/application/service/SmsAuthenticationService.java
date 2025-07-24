package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.PhoneAuthentication;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.domain.type.AuthType;
import com.ons.back.persistence.repository.PhoneAuthenticationRepository;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.request.SMSAuthRequest;
import com.ons.back.presentation.dto.request.SendMessageRequest;
import com.ons.back.presentation.dto.response.CheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class SmsAuthenticationService {

    private final SmsSendService smsSendService;
    private final PhoneAuthenticationRepository phoneAuthenticationRepository;
    private final UserRepository userRepository;

    public void sendSMSAuth(SendMessageRequest request){

        String authCode = createCode();

        smsSendService.sendSms(request, authCode);

        List<PhoneAuthentication> phoneAuthenticationList = phoneAuthenticationRepository.findByPhoneNumberAndAuthType(request.to(), AuthType.JOIN);
        phoneAuthenticationList.forEach(emailAuthentication -> emailAuthentication.updateIsActive((byte)0));
        PhoneAuthentication phoneAuthentication = request.toResetEntity(authCode);
        phoneAuthenticationRepository.save(phoneAuthentication);
    }

    public CheckResponse checkSMSAuthCode(SMSAuthRequest request) {

        PhoneAuthentication phoneAuthentication = phoneAuthenticationRepository.findByPhoneNumberAndAuthCodeAndIsActiveAndCreatedAtAfter(request.phoneNumber(), request.authCode(), (byte)1, LocalDateTime.now().minusMinutes(10))
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("일치하지 않는 인증 코드입니다.", 400, LocalDateTime.now())
                ));

        phoneAuthentication.updateIsActive((byte)0);

        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("유저를 찾을 수 없습니다.", 404, LocalDateTime.now())
                ));

        return CheckResponse.fromUser(user);
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
