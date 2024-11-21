package com.spring.citronix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1000, message = "Area must be at least 1000 m²")
    private double area;

    @ManyToOne(optional = false)
    private Farm farm;

    @OneToMany(mappedBy = "field")
    @JsonIgnoreProperties({"field"})
    private List<Tree> trees;

    public boolean isTreeDensityValid(int numberOfTrees) {
        return numberOfTrees <= area * 10;
    }

    public boolean isAreaValid() {
        return area < (farm.getArea() * 0.5);
    }
}
