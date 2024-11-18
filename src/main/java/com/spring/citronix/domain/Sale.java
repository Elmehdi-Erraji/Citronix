package com.spring.citronix.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private java.time.LocalDate date;

    @Column(nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private String client;

    @Column(nullable = false)
    private double revenue;

    @Column(nullable = false)
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "harvest_id", nullable = false)
    private Harvest harvest;

    public double calculateRevenue(double quantity) {
        return quantity * unitPrice;
    }
}
