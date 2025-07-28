package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.*;
import com.ons.back.persistence.domain.type.OrderStatusType;
import com.ons.back.persistence.domain.type.PaymentType;
import com.ons.back.persistence.repository.*;
import com.ons.back.presentation.dto.request.CreateItemRequest;
import com.ons.back.presentation.dto.request.UpdateItemRequest;
import com.ons.back.presentation.dto.response.ReadItemResponse;
import com.ons.back.presentation.dto.response.ReadLowStockItemResponse;
import com.ons.back.presentation.dto.response.ReadOrderItemResponse;
import com.ons.back.presentation.dto.response.ReadSaledItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final StorageService storageService;

    public List<ReadItemResponse> getItemsByStoreId(String userKey, Long storeId, String sortType, Boolean isOrdered, Boolean saleStatus) {

        validUserStore(userKey, storeId);

        List<Item> itemList = new ArrayList<>();

        if(saleStatus != null){
            if(saleStatus) {
                itemList.addAll(itemRepository.findByStore_StoreIdAndIsActiveTrueAndItemStockGreaterThan(storeId, 1));
            } else {
                itemList.addAll(itemRepository.findByStore_StoreIdAndItemStockAndIsActiveTrue(storeId, 0));
            }
        }

        if(isOrdered != null) {
            if(isOrdered) {
                itemList.addAll(itemRepository.findByStore_StoreIdAndIsOrderedAndIsActiveTrue(storeId, true));
            } else {
                itemList.addAll(itemRepository.findByStore_StoreIdAndIsOrderedAndIsActiveTrue(storeId, false));
            }
        }

        if(isOrdered == null && saleStatus == null) {
            itemList.addAll(itemRepository.findByStore_StoreIdAndIsActiveTrue(storeId));
        }

        Comparator<Item> comparator = switch (sortType.toLowerCase()) {
            case "created_at_desc" -> Comparator.comparing(Item::getCreatedAt);
            case "item_name_desc" -> Comparator.comparing(Item::getItemName);
            case "item_name_asc" -> Comparator.comparing(Item::getItemName).reversed();
            case "item_price_desc" -> Comparator.comparing(Item::getItemPurchasePrice);
            case "item_price_asc" -> Comparator.comparing(Item::getItemPurchasePrice).reversed();
            case "item_quantity_desc" -> Comparator.comparing(Item::getItemStock);
            case "item_quantity_asc" -> Comparator.comparing(Item::getItemStock).reversed();
            default -> Comparator.comparing(Item::getCreatedAt).reversed();
        };

        return itemList.stream()
                .sorted(comparator)
                .map(ReadItemResponse::fromEntity)
                .toList();
    }

    public List<ReadSaledItemResponse> getSaledItem(LocalDateTime start, LocalDateTime end, Long storeId, String userKey) {

        Store store = validUserStore(userKey, storeId);

        Map<Item, Integer> itemSalesMap = new HashMap<>();
        List<ReadSaledItemResponse> response = new ArrayList<>();

        List<Order> orderList = orderRepository.findByStoreAndCreatedAtBetweenAndOrderStatus(store, start, end, OrderStatusType.SUCCESS);

        for(Order order : orderList) {
            List<OrderItem> orderItems = order.getOrderItemList();

            for(OrderItem orderItem : orderItems) {
                Item item = orderItem.getItem();
                Integer currentQuantity = itemSalesMap.getOrDefault(item, 0);
                itemSalesMap.put(item, currentQuantity + orderItem.getQuantity());
            }
        }

        for(Item item : itemSalesMap.keySet()) {
            Integer quantity = itemSalesMap.get(item);
            response.add(ReadSaledItemResponse.from(item, quantity));
        }

        response.sort(Comparator.comparingInt(ReadSaledItemResponse::saleQuantity));

        return response;
    }

    public List<ReadLowStockItemResponse> getLowStockItem(String userKey, Long storeId) {
        Store store = validUserStore(userKey, storeId);

        return itemRepository.findTop4ByStoreAndIsActiveTrueOrderByItemStockAsc(store).stream().map(ReadLowStockItemResponse::fromEntity).toList();
    }

    public List<ReadLowStockItemResponse> getLowStockItemFalse(String userKey, Long storeId) {
        Store store = validUserStore(userKey, storeId);

        return itemRepository.findTop4ByStoreAndIsActiveTrueAndIsOrderedFalseOrderByItemStockAsc(store).stream().map(ReadLowStockItemResponse::fromEntity).toList();
    }

    public void createItem(String userKey, CreateItemRequest request, MultipartFile file) {

        Store store = validUserStore(userKey, request.storeId());

        String itemImage = null;

        if(file != null) {
            itemImage = storageService.uploadImage(file);
        }

        itemRepository.save(request.toEntity(store, itemImage));
    }

    public void updateItem(String userKey, UpdateItemRequest request) {

        validUserStore(userKey, request.storeId());

         Item item = itemRepository.findById(request.itemId())
                 .orElseThrow(() -> new ApplicationException(
                         ErrorStatus.toErrorStatus("해당하는 아이템이 없습니다.", 404, LocalDateTime.now())
                 ));

         if(request.isOrdered() != null && !request.isOrdered().equals(item.getIsOrdered())) {
             item.updateIsOrdered(request.isOrdered());
         }

         if(request.itemStock() != null && !request.itemStock().equals(item.getItemStock())) {
             item.updateItemStock(request.itemStock());
         }

         if(request.itemPrice() != null && !request.itemPrice().equals(item.getItemPrice())) {
             item.updateItemPrice(request.itemPrice());
         }

         if(request.itemPurchasePrice() != null && request.itemPurchasePrice().equals(item.getItemPurchasePrice())) {
             item.updateItemPurchasePrice(request.itemPurchasePrice());
         }

         if(request.barcode() != null && !request.barcode().equals(item.getBarcode())) {
             item.updateBarcode(request.barcode());
         }
    }

    public void deleteItem(String userKey, Long itemId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 아이템이 없습니다.", 404, LocalDateTime.now())
                ));

        User user = userRepository.findByUserKey(userKey)
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 유저가 없습니다.", 404, LocalDateTime.now())
                ));

        if(!user.equals(item.getStore().getUser())) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("가게의 주인이 아닙니다.", 404, LocalDateTime.now())
            );
        }

        item.delete();
    }

    private Store validUserStore(String userKey, Long storeId) {

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
