package com.ons.back.persistence.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "inquiry")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Column(name = "inquiry_phone_number")
    private String phoneNumber;

    @Column(name = "inquiry_email")
    private String email;

    @Column(name = "inquiry_name")
    private String name;

    @Column(name = "store_type")
    private String storeType;

    @Column(name = "inquiry_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "inquiry_answer")
    private String answer;

    @Column(name = "is_answered")
    private Boolean isAnswered = false;

    @Column(name = "agree_privacy")
    private Boolean agreePrivacy;

    @PrePersist
    public void prePersist() {
        this.isAnswered = false;
    }

    public void updateAnswer(String answer) {
        this.answer = answer;
    }

    @Builder
    public Inquiry(Long id, String phoneNumber, String email, String name, String storeType, String content, String answer, Boolean isAnswered, Boolean agreePrivacy) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.storeType = storeType;
        this.content = content;
        this.answer = answer;
        this.isAnswered = isAnswered;
        this.agreePrivacy = agreePrivacy;
    }
}
