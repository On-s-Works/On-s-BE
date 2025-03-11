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

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        String imageUrl = storageService.uploadFirebaseBucket(file, "ons" + file.getOriginalFilename());

        storeRepository.save(request.toEntity(user, imageUrl));
    }

    public void updateStoreImage(String userKey, Long storeId, MultipartFile file) {
        Store store = validateStoreOwner(userKey, storeId);
        storageService.deleteFirebaseBucket(store.getStoreImage());
        store.updateImage(storageService.uploadFirebaseBucket(file, "ons" + file.getOriginalFilename()));
    }

    public void updateStore(String userKey, UpdateStoreRequest request) {
        Store store = validateStoreOwner(userKey, request.storeId());

        if(request.storeName() != null) {
            store.updateName(request.storeName());
        }

        if(request.storeNumber() != null) {
            store.updateNumber(request.storeNumber());
        }

        if(request.isManage() != store.isManage()) {
            store.updateIsManage(request.isManage());
        }

        if(request.isSale() != store.isSale()) {
            store.updateIsSale(request.isSale());
        }

        if(request.storeType() != null) {
            store.updateStoreType(request.storeType());
        }

        if(request.baseAddress() != null && request.addressDetail() != null) {
            store.updateAddress(request.baseAddress(), request.addressDetail());
        }
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
