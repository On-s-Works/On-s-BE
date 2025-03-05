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
@Table(name = "visit")
public class Visit {

    @Id
    @Column(name = "visit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitId;

    @Column(name = "visit_time")
    private LocalDateTime visitTime;

    @Column(name = "visit_count")
    private Long visitCount;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Visit(Long visitId, LocalDateTime visitTime, Long visitCount, Store store) {
        this.visitId = visitId;
        this.visitTime = visitTime;
        this.visitCount = visitCount;
        this.store = store;
    }
}
