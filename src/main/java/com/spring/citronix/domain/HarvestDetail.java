package com.spring.citronix.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HarvestDetail {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private double quantity;

    @ManyToOne(optional = false)
    private Tree tree;

    @ManyToOne
    private Field field;

    @ManyToOne
    @JoinColumn(name = "harvest_id")
    private Harvest harvest;
}
