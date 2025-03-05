package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Item;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.repository.ItemRepository;
import com.ons.back.persistence.repository.StoreRepository;
import com.ons.back.presentation.dto.request.CreateItemRequest;
import com.ons.back.presentation.dto.request.UpdateItemRequest;
import com.ons.back.presentation.dto.response.ReadItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;

    public List<ReadItemResponse> getItemsByStoreId(Long userId, Long storeId) {

        validUserStore(userId, storeId);

        return itemRepository.findByStore_StoreId(storeId).stream().map(ReadItemResponse::fromEntity).toList();
    }

    public void createItem(Long userId, CreateItemRequest request) {

        Store store = validUserStore(userId, request.storeId());

        itemRepository.save(request.toEntity(store));
    }

    //뭐 변경할 수 있을지
    public void updateItem(Long userId, UpdateItemRequest request) {

         Item item = itemRepository.findById(request.itemId())
                 .orElseThrow(() -> new ApplicationException(
                         ErrorStatus.toErrorStatus("해당하는 아이템이 없습니다.", 404, LocalDateTime.now())
                 ));

        if(!storeRepository.findByUser_UserId(userId).contains(item.getStore())) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("권한이 없는 가게입니다.", 401, LocalDateTime.now())
            );
        }

         //item update
    }

    public void deleteItem(Long userId, Long itemId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 아이템이 없습니다.", 404, LocalDateTime.now())
                ));

        if(!storeRepository.findByUser_UserId(userId).contains(item.getStore())) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("권한이 없는 가게입니다.", 401, LocalDateTime.now())
            );
        }

        item.delete();
    }

    private Store validUserStore(Long userId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                        .orElseThrow(() -> new ApplicationException(
                                ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                        ));

        if(!storeRepository.findByUser_UserId(userId).contains(store)) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("권한이 없는 가게입니다.", 401, LocalDateTime.now())
            );
        }
        return store;
    }
}
