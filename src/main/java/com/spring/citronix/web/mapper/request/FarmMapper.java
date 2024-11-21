package com.spring.citronix.web.mapper.request;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.web.vm.request.farm.FarmCreateVM;
import com.spring.citronix.web.vm.request.farm.FarmUpdateVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FarmMapper {

    // Map CreateFarmVM to Farm
    @Mapping(target = "id", ignore = true)
    Farm toEntity(FarmCreateVM createFarmVM);

    Farm toEntity(FarmUpdateVM updateFarmVM);

    FarmCreateVM toCreateVM(Farm farm);

    FarmUpdateVM toUpdateVM(Farm farm);

}
