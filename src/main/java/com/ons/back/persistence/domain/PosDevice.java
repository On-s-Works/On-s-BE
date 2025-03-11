package com.ons.back.persistence.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "pos_device")
public class PosDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pos_device_id")
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public PosDevice(Long id, String serialNumber, Store store) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.store = store;
    }
}
