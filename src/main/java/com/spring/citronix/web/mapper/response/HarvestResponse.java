package com.spring.citronix.web.mapper.response;


import com.spring.citronix.domain.enums.Season;
import com.spring.citronix.web.vm.response.harvest.HarvestDetailResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class HarvestResponse {
    private UUID id;
    private LocalDate harvestDate;
    private Season season;
    private Double totalQuantity;
    private List<HarvestDetailResponse> harvestDetails;
}
