package com.spring.citronix.web.mapper.request;

import com.spring.citronix.domain.Field;
import com.spring.citronix.web.vm.request.field.FieldCreateVM;
import com.spring.citronix.web.vm.response.field.FieldResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FieldMapper {

    @Mapping(source = "id", target = "fieldId")
    @Mapping(source = "area", target = "fieldArea")
    @Mapping(source = "farm.id", target = "farmId")
    @Mapping(source = "farm.name", target = "farmName")
    FieldResponseVM toFieldResponseVM(Field field);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "farmId", target = "farm.id")
    Field toEntity(FieldCreateVM fieldCreateVM);

    void updateEntityFromVM(FieldCreateVM fieldCreateVM, @MappingTarget Field field);
}
