package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Field;
import com.spring.citronix.repository.FieldRepository;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.web.errors.field.FieldNotFoundException;
import com.spring.citronix.web.errors.field.InvalidTreeDensityException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FieldServiceImp implements FieldService {
    private final FieldRepository fieldRepository;


    public FieldServiceImp(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @Override
    public Field save(Field field) {
        return fieldRepository.save(field);
    }

    @Override
    public void delete(UUID id) {
        if (!fieldRepository.existsById(id)) {
            throw new FieldNotFoundException(id);
        }
        fieldRepository.deleteById(id);
    }

    @Override
    public List<Field> findByFarmId(UUID farmId) {
        return fieldRepository.findByFarmId(farmId);
    }

    @Override
    public boolean isTreeDensityValid(UUID fieldId, int numberOfTrees) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException(fieldId));

        if (!field.isTreeDensityValid(numberOfTrees)) {
            throw new InvalidTreeDensityException(fieldId, numberOfTrees);
        }

        return true;

    }

    @Override
    public Optional<Field> findById(UUID id) {
        return fieldRepository.findById(id);
    }
}
