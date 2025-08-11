package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.repository.StoreRepository;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.request.UpdateSocialLoginUserRequest;
import com.ons.back.presentation.dto.response.ReadUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public ReadUserResponse getUserByKey(String userKey) {

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("유저를 찾을 수 없습니다.", 404, LocalDateTime.now())
                ));

        return ReadUserResponse.fromEntity(user);
    }

    public void updatePassword(String userKey, String newPassword) {

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("유저를 찾을 수 없습니다.", 404, LocalDateTime.now())
                ));

        user.updatePassword(passwordEncoder.encode(newPassword));
    }

    public void updateTermsAndPhoneNumber(String userKey, UpdateSocialLoginUserRequest request) {

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("유저를 찾을 수 없습니다.", 404, LocalDateTime.now())
                ));

        if(request.agreeTerms() != null) {
            user.updateAgreeTerms(request.agreeTerms());
        }

        if(request.phoneNumber() != null) {
            user.updatePhoneNumber(request.phoneNumber());
        }
    }

    public void deleteByUserKey(String userKey) {

        User user = userRepository.findByUserKey(userKey)
                        .orElseThrow(() -> new ApplicationException(
                                ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                        ));

        List<Store> storeList = storeRepository.findByUser_UserId(user.getUserId());

        storeList.forEach(Store::delete);
        storeList.forEach(store -> store.updateUser(null));
        storeRepository.flush();

        userRepository.deleteByUserKey(userKey);
    }
}
