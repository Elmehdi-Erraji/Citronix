package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;
import com.spring.citronix.repository.FieldRepository;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.web.errors.field.FieldNotFoundException;
import com.spring.citronix.web.errors.field.InvalidTreeDensityException;
import com.spring.citronix.web.errors.field.InvalidFieldAreaException;
import com.spring.citronix.web.errors.field.MaxFieldsInFarmException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        validateFieldArea(field);
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
    public Page<Field> findByFarmId(UUID farmId, Pageable pageable) {
        return fieldRepository.findByFarmId(farmId, pageable);
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


    private void validateFieldArea(Field field)  {
        double farmArea = field.getFarm().getArea();

        System.out.println(farmArea);
        double maxAllowedSingleFieldArea = field.getFarm().getArea() / 2;
        System.out.println(maxAllowedSingleFieldArea);

        if (field.getFarm().getFields().size() >= 10) {
            throw new MaxFieldsInFarmException("A farm cannot have more than 10 fields");
        }

        if (field.getArea() > maxAllowedSingleFieldArea) {
            throw new InvalidFieldAreaException(
                    String.format("Field area cannot exceed 50%% of the farm's total area. Max allowed: %.2f", maxAllowedSingleFieldArea)
            );
        }

        double totalExistingFieldsArea = fieldRepository.findFieldByFarmId(field.getFarm().getId())
                .stream()
                .mapToDouble(Field::getArea)
                .sum();
        double totalAfterAddingNewField = totalExistingFieldsArea + field.getArea();

        if (totalAfterAddingNewField > farmArea) {
            throw new InvalidFieldAreaException(
                    String.format("Total fields' area cannot exceed the farm's total area. Current total: %.2f, Farm area: %.2f",
                            totalAfterAddingNewField, farmArea)
            );
        }
    }
}
