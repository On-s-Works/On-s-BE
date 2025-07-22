package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.StoreUser;
import com.ons.back.persistence.domain.StoreUserMemo;
import com.ons.back.persistence.repository.StoreUserMemoRepository;
import com.ons.back.persistence.repository.StoreUserRepository;
import com.ons.back.presentation.dto.request.CreateStoreUserMemoRequest;
import com.ons.back.presentation.dto.request.DeleteStoreUserMemoRequest;
import com.ons.back.presentation.dto.request.UpdateStoreUserMemoRequest;
import com.ons.back.presentation.dto.response.ReadStoreUserMemoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreUserMemoService {

    private final StoreUserMemoRepository storeUserMemoRepository;
    private final StoreUserRepository storeUserRepository;

    public Long create(CreateStoreUserMemoRequest request) {

        StoreUser storeUser = storeUserRepository.findById(request.storeUserId())
                        .orElseThrow(() -> new ApplicationException(
                                ErrorStatus.toErrorStatus("해당하는 스토어 고객이 없습니다.", 404, LocalDateTime.now())
                        ));

        return storeUserMemoRepository.save(request.toEntity(storeUser)).getId();
    }

    public void update(UpdateStoreUserMemoRequest request) {

        StoreUserMemo storeUserMemo = storeUserMemoRepository.findById(request.id())
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 메모가 없습니다.", 404, LocalDateTime.now())
                ));

        storeUserMemo.updateContent(request.content());
    }

    public void delete(DeleteStoreUserMemoRequest request) {
        storeUserMemoRepository.deleteById(request.id());
    }

    @Transactional(readOnly = true)
    public List<ReadStoreUserMemoResponse> getByStoreUserId(Long storeUserId) {
        return storeUserMemoRepository.findByStoreUser_Id(storeUserId).stream().map(ReadStoreUserMemoResponse::fromEntity).toList();
    }
}
