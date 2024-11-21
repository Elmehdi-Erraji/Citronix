package com.spring.citronix.domain;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

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
    @NotBlank(message = "Farm name is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Farm location is required")
    private String location;

    @Column(nullable = false)
    @Positive(message = "Farm area must be greater than zero")
    private double area;

    @Column(nullable = false)
    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDate creationDate;

    @OneToMany
    private List<Field> fields= new ArrayList<>();

    public boolean isValidArea(double fieldAreaSum) {
        return fieldAreaSum < this.area;
    }
}
