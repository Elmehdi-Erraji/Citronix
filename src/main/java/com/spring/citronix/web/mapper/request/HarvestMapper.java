package com.spring.citronix.web.mapper.request;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.web.vm.request.harvest.HarvestCreateVM;
import com.spring.citronix.web.vm.response.harvest.HarvestResponseVM;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HarvestMapper {

    @Mapping(target = "farm.id", source = "farmId")
    @Mapping(target = "season", expression = "java(com.spring.citronix.domain.enums.Season.valueOf(harvestCreateVM.getSeason()))")
    Harvest toEntity(@Valid HarvestCreateVM harvestCreateVM);

    @Mapping(target = "season", expression = "java(harvest.getSeason().name())")
    HarvestResponseVM toResponseVM(Harvest harvest);
}
