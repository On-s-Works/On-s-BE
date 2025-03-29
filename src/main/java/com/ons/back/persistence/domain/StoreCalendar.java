package com.ons.back.persistence.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "store_calendar")
public class StoreCalendar {

    @Id
    @Column(name = "store_calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "day")
    private Integer day;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public StoreCalendar(Long id, Integer year, Integer month, Integer day, String content, Store store) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = content;
        this.store = store;
    }
}
