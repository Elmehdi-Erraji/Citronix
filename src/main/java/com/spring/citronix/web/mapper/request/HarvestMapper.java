package com.spring.citronix.web.mapper.request;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.HarvestDetail;
import com.spring.citronix.web.vm.request.harvest.HarvestRequestVM;
import com.spring.citronix.web.vm.response.harvest.HarvestDetailResponse;
import com.spring.citronix.web.vm.response.harvest.HarvestResponse;
import com.spring.citronix.web.vm.response.harvest.HarvestResponseVM;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HarvestMapper {

    HarvestResponseVM toHarvestResponseVM(Harvest harvest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "harvestDetails", ignore = true)
    @Mapping(target = "sales", ignore = true)
    Harvest toEntity(HarvestRequestVM request);

    HarvestResponse toResponse(Harvest harvest);

}
