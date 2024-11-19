package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;

import com.spring.citronix.repository.FarmRepository;
import com.spring.citronix.repository.FieldRepository;
import com.spring.citronix.service.FarmService;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.web.vm.request.farm.FarmDTO;
import com.spring.citronix.web.vm.request.farm.ListField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FarmServiceWithFields implements FarmService {
    private final FarmRepository farmRepository;
    private final FieldRepository fieldRepository;
    private final FieldService fieldService;

    @Override
    public Farm save(Farm farm) {
        return null;
    }

    @Override
    @Transactional
    public Farm saveFromDTO(Farm farm) {
        Farm savedFarm = farmRepository.save(farm);

        if (savedFarm.getFields() != null) {
            double totalFieldArea = savedFarm.getFields().stream()
                    .mapToDouble(Field::getArea)
                    .sum();

            if (!savedFarm.isValidArea(totalFieldArea)) {
                throw new IllegalArgumentException("Total area of fields exceeds farm area");
            }

            List<Field> savedFields = savedFarm.getFields().stream()
                    .map(field -> {
                        field.setFarm(savedFarm);
                        return fieldRepository.save(field);
                    })
                    .toList();

            savedFarm.setFields(savedFields);
        }

        return savedFarm;
    }

    @Override
    public Optional<Farm> findById(UUID id) {
        return farmRepository.findById(id);
    }

    @Override
    public List<Farm> findAll() {
        return farmRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Farm farm) {
        Optional<Farm> existingFarm = farmRepository.findById(farm.getId());
        if (existingFarm.isEmpty()) {
            throw new IllegalArgumentException("Farm not found.");
        }
        if (farm.getFields() != null) {
            fieldRepository.deleteAll(farm.getFields());
        }

        farmRepository.delete(farm);
    }

    @Override
    public List<Farm> searchFarms(String name, String location, LocalDate startDate) {
        return List.of();
    }

    @Override
    public List<Farm> getFarmsWithAreaLessThan4000() {
        List<Farm> allFarms = farmRepository.findAll();
        List<Farm> result = new ArrayList<>();

        for (Farm farm : allFarms) {
            double totalArea = 0;
            for (Field field : farm.getFields()) {
                totalArea += field.getArea();
            }
            if (totalArea < 4000) {
                result.add(farm);
            }
        }

        return result;
    }
}
