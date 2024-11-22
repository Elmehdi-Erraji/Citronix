package com.spring.citronix.web.mapper.request;

import com.spring.citronix.domain.Sale;
import com.spring.citronix.web.vm.response.sale.SaleResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "harvestId", source = "sale.harvest.id")
    SaleResponseVM toSaleResponseVM(Sale sale);

}
