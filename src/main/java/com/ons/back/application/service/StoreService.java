package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.domain.User;
import com.ons.back.persistence.repository.StoreRepository;
import com.ons.back.persistence.repository.UserRepository;
import com.ons.back.presentation.dto.request.CreateStoreRequest;
import com.ons.back.presentation.dto.request.UpdateStoreRequest;
import com.ons.back.presentation.dto.response.ReadStoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    @Transactional(readOnly = true)
    public List<ReadStoreResponse> getStoreByUserId(String userKey) {

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        return storeRepository.findByUser_UserId(user.getUserId()).stream().map(ReadStoreResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public ReadStoreResponse getStoreById(Long storeId) {
        return ReadStoreResponse.fromEntity(storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                )));
    }

    public void createStore(String userKey, CreateStoreRequest request, MultipartFile file) {

        String imageUrl = storageService.uploadFirebaseBucket(file, "ons" + file.getOriginalFilename());

        User user = userRepository.findByUserKey(userKey)
                        .orElseThrow(() -> new ApplicationException(
                                ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                        ));

        storeRepository.save(request.toEntity(user, imageUrl));
    }

    public void updateStoreName(String userKey, UpdateStoreRequest request) {
        Store store = validateStoreOwner(userKey, request.storeId());
        store.updateName(request.storeName());
    }

    public void updateStoreAddress(String userKey, UpdateStoreRequest request) {
        Store store = validateStoreOwner(userKey, request.storeId());
        store.updateAddress(request.baseAddress(), request.addressDetail());
    }

    public void updateStoreType(String userKey, UpdateStoreRequest request) {
        Store store = validateStoreOwner(userKey, request.storeId());
        store.updateStoreType(request.storeType());
    }

    public void deleteStore(String userKey, Long storeId) {
        Store store = validateStoreOwner(userKey, storeId);
        store.delete();
    }

    private Store validateStoreOwner(String userKey, Long storeId) {
        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        if(!user.equals(store.getUser())) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("가게의 주인이 아닙니다.", 404, LocalDateTime.now())
            );
        }
        return store;
    }
}
