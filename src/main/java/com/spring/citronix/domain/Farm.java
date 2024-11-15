package com.spring.citronix.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Farm {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double area;

    @Column(nullable = false)
    private java.time.LocalDate creationDate;

    public boolean isValidArea(double fieldAreaSum) {
        return fieldAreaSum < this.area;
    }
}
