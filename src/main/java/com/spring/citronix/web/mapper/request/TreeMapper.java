package com.spring.citronix.web.mapper.request;

import com.spring.citronix.domain.Tree;
import com.spring.citronix.web.vm.request.tree.TreeCreateVM;
import com.spring.citronix.web.vm.response.tree.TreeResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreeMapper {
    @Mapping(target = "field.id", source = "fieldId")
    Tree toEntity(TreeCreateVM treeCreateVM);

    @Mapping(target = "age", expression = "java(tree.calculateAge())")
    @Mapping(target = "productivity", expression = "java(tree.calculateProductivity())")
    TreeResponseVM toResponseVM(Tree tree);
}
