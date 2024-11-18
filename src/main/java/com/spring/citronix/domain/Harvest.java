package com.spring.citronix.domain;

import com.spring.citronix.domain.enums.Season;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Harvest {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Season season;

    @Column(nullable = false)
    private LocalDate harvestDate;

    @Column(nullable = false)
    private double totalQuantity;


    @ManyToOne
    private Farm farm;

    @OneToMany(mappedBy = "harvest")
    private List<HarvestDetail> harvestDetails;

    @OneToMany(mappedBy = "harvest")
    private List<Sale> sales;
}
