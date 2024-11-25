package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Field;
import com.spring.citronix.domain.Tree;
import com.spring.citronix.repository.FieldRepository;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.service.TreeService;
import com.spring.citronix.web.errors.FieldNotFoundException;
import com.spring.citronix.web.errors.InvalidTreeDensityException;
import com.spring.citronix.web.errors.InvalidFieldAreaException;
import com.spring.citronix.web.errors.MaxFieldsInFarmException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FieldServiceImp implements FieldService {
    private final FieldRepository fieldRepository;
    private final TreeService treeService;
    private final HarvestDetailService harvestDetailService;

    public FieldServiceImp(FieldRepository fieldRepository, TreeService treeService, HarvestDetailService harvestDetailService) {
        this.fieldRepository = fieldRepository;
        this.treeService = treeService;
        this.harvestDetailService = harvestDetailService;
    }

    @Override
    public Field save(Field field) {
        validateFieldArea(field);
        return fieldRepository.save(field);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Optional<Field> field = findById(id);
        if (field.isPresent()) {
            List<Tree> trees = treeService.findByFieldId(id);
            for (Tree tree : trees) {
                treeService.delete(tree.getId());
            }

            fieldRepository.delete(field.get());
        } else {
            throw new EntityNotFoundException("Field not found with ID: " + id);
        }
    }

    @Override
    public Page<Field> findByFarmId(UUID farmId, Pageable pageable) {
        return fieldRepository.findByFarmId(farmId, pageable);
    }

    @Override
    public List<Field> findByFarmId(UUID farmId) {
        return List.of();
    }

    public List<Field> findByFatrId(UUID id){
        return fieldRepository.findFieldByFarmId(id);
    }

    @Override
    public boolean isTreeDensityValid(UUID fieldId, int numberOfTrees) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("field not found"));

        if (!field.isTreeDensityValid(numberOfTrees)) {
            throw new InvalidTreeDensityException(fieldId, numberOfTrees);
        }

        return true;
    }

    public Optional<Field> findById(UUID fieldId) {
        return Optional.ofNullable(fieldRepository.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field not found with id: ")));
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
