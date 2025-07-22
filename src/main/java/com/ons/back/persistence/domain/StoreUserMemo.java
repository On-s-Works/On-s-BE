package com.ons.back.persistence.domain;

import com.ons.back.presentation.dto.request.UpdateStoreUserMemoRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "store_user_memo")
public class StoreUserMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_user_id")
    private StoreUser storeUser;

    public void updateContent(String content) {
        this.content = content;
    }

    @Builder
    public StoreUserMemo(Long id, String content, StoreUser storeUser) {
        this.id = id;
        this.content = content;
        this.storeUser = storeUser;
    }
}
