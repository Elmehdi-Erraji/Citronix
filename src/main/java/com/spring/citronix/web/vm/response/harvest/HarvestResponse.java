package com.spring.citronix.web.vm.response.harvest;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class HarvestResponse {
    private UUID id;
    private String season;
    private LocalDate harvestDate;
    private Double totalQuantity;
}
