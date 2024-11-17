package com.spring.citronix.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Tree {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate plantingDate;

    @ManyToOne(optional = false)
    private Field field;


    public int calculateAge() {
        return Period.between(plantingDate, LocalDate.now()).getYears();
    }

    public double calculateProductivity() {
        int age = calculateAge();
        if (age < 3) return 2.5;
        else if (age <= 10) return 12.0;
        else if (age <= 20) return 20.0;
        else return 0.0;
    }
}
