package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;
import com.spring.citronix.repository.FarmRepository;
import com.spring.citronix.repository.specifications.FarmSpecification;
import com.spring.citronix.service.FarmService;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.web.errors.FarmNotFoundException;
import com.spring.citronix.web.errors.InvalidFarmException;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FarmServiceImp implements FarmService {

    private final FarmRepository farmRepository;
    private final FieldService fieldService;

    public FarmServiceImp(FarmRepository farmRepository, FieldService fieldService) {
        this.farmRepository = farmRepository;
        this.fieldService = fieldService;
    }

    @Override
    public Farm save(@Valid Farm farm) {
        validateFarm(farm);
        fieldAreaSumCheck(farm);
        return farmRepository.save(farm);
    }

    @Override
    public Optional<Farm> findById(UUID id) {
        Optional<Farm> farm = farmRepository.findById(id);
        if (!farm.isPresent()) {
            throw new FarmNotFoundException(id);
        }
        return farm;
    }

    @Override
    public Page<Farm> findAll(Pageable pageable) {
        return farmRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public void delete(Farm farm) {
        List<Field> fields = fieldService.findByFarmId(farm.getId());
        for (Field field : fields) {
            fieldService.delete(field.getId());
        }

        farmRepository.delete(farm);
    }

    @Override
    public List<Farm> searchFarms(String name, String location, LocalDate startDate) {
        Specification<Farm> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and(FarmSpecification.nameContains(name));
        }
        if (location != null && !location.isEmpty()) {
            spec = spec.and(FarmSpecification.locationContains(location));
        }
        if (startDate != null) {
            spec = spec.and(FarmSpecification.creationDateAfter(startDate));
        }

        return farmRepository.findAll(spec);
    }

    private void validateFarm(Farm farm) {
        if (farm.getName() == null || farm.getName().trim().isEmpty()) {
            throw new InvalidFarmException("Farm name is required.");
        }
        if (farm.getLocation() == null || farm.getLocation().trim().isEmpty()) {
            throw new InvalidFarmException("Farm location is required.");
        }
        if (farm.getArea() <= 0) {
            throw new InvalidFarmException("Farm area must be greater than 0.");
        }
        if (farm.getCreationDate() == null) {
            throw new InvalidFarmException("Creation date is required.");
        }
        if (farm.getCreationDate().isAfter(LocalDate.now())) {
            throw new InvalidFarmException("Creation date cannot be in the future.");
        }
    }

    private void fieldAreaSumCheck(Farm farm) {
        if (farm.getFields() != null && !farm.getFields().isEmpty()) {
            double totalFieldArea = farm.getFields().stream()
                    .mapToDouble(Field::getArea)
                    .sum();
            if (!farm.isValidArea(totalFieldArea)) {
                throw new InvalidFarmException("Total area of fields > the farm's available area.");
            }

            for (Field field : farm.getFields()) {
                if (field.getArea() <= 0) {
                    throw new InvalidFarmException("Field area must be more than 0.");
                }
            }
        }
    }


}
