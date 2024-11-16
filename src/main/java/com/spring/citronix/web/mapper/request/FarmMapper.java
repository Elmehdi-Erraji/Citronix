package com.spring.citronix.web.mapper.request;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.web.vm.request.farm.FarmCreateVM;
import com.spring.citronix.web.vm.request.farm.FarmUpdateVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FarmMapper {

    // Map CreateFarmVM to Farm
    @Mapping(target = "id", ignore = true) // ID is generated, not mapped from VM
    Farm toEntity(FarmCreateVM createFarmVM);

    // Map UpdateFarmVM to Farm
    Farm toEntity(FarmUpdateVM updateFarmVM);

    // Map Farm to CreateFarmVM (if needed)
    FarmCreateVM toCreateVM(Farm farm);

    // Map Farm to UpdateFarmVM (if needed)
    FarmUpdateVM toUpdateVM(Farm farm);
}
