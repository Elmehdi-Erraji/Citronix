package com.spring.citronix.web.vm.response.sale;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class SaleResponseVM {
    private UUID id;
    private LocalDate date;
    private double unitPrice;
    private String client;
    private double revenue;
    private double quantity;
    private UUID harvestId;
}
