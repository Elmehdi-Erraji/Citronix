package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.repository.FarmRepository;
import com.spring.citronix.repository.specifications.FarmSpecification;
import com.spring.citronix.service.FarmService;
import com.spring.citronix.web.errors.farm.FarmNotFoundException;
import com.spring.citronix.web.errors.farm.InvalidFarmException;
import com.spring.citronix.web.vm.request.farm.FarmDTO;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FarmServiceImp implements FarmService {

    private final FarmRepository farmRepository;

    public FarmServiceImp(FarmRepository farmRepository) {
        this.farmRepository = farmRepository;
    }

    @Override
    public Farm save(@Valid Farm farm) {
        validateFarm(farm);
        return farmRepository.save(farm);
    }

    @Override
    public Farm saveFromDTO(Farm farm) {
        return null;
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
    public List<Farm> findAll() {
        return farmRepository.findAll();
    }

    @Override
    public void delete(Farm farm) {
        farmRepository.delete(farm);
    }

    @Override
    public List<Farm> getFarmsWithAreaLessThan4000() {
        return List.of();
    }

    @Override
    public List<Farm> searchFarms(String name, String location, LocalDate startDate) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidFarmException("Farm name cannot be empty.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new InvalidFarmException("Farm location cannot be empty.");
        }
        if (startDate == null) {
            throw new InvalidFarmException("Start date cannot be null.");
        }

        return farmRepository.findAll(
                Specification
                        .where(FarmSpecification.nameContains(name))
                        .and(FarmSpecification.locationContains(location))
                        .and(FarmSpecification.creationDateAfter(startDate))
        );
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
    }
}
