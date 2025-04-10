package com.ons.back.persistence.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "store_calendar")
public class StoreCalendar {

    @Id
    @Column(name = "store_calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_datetime")
    LocalDateTime start;

    @Column(name = "end_datetime")
    LocalDateTime end;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public StoreCalendar(Long id, LocalDateTime start, LocalDateTime end, String content, Store store) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.content = content;
        this.store = store;
    }
}
