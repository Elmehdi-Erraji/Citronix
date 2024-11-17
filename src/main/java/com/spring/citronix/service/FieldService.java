package com.spring.citronix.service;

import com.spring.citronix.domain.Field;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FieldService {
    Field save(Field field);
    Optional<Field> findById(UUID id);
    void delete(UUID id);
    List<Field> findByFarmId(UUID farmId);
    boolean isTreeDensityValid(UUID fieldId, int numberOfTrees);
}
