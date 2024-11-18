package com.spring.citronix.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private double area;

    @ManyToOne(optional = false)
    private Farm farm;

    @OneToMany(mappedBy = "field")
    private List<Tree> trees;

    public boolean isTreeDensityValid(int numberOfTrees) {
        return numberOfTrees <= area * 100;
    }
}
