package com.spring.citronix.web.vm.request.harvest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HarvestRequestVM {

    @NotNull(message = "Harvest date cannot be null")
    @PastOrPresent(message = "Harvest date must be in the past or present")
    private LocalDate harvestDate;

    @NotNull(message = "Entity ID is required")
    private UUID item;

}
