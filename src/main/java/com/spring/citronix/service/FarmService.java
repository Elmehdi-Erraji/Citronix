package com.spring.citronix.service;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.repository.FarmRepository;
import com.spring.citronix.repository.specifications.FarmSpecification;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class FarmService {

    private final FarmRepository farmRepository;

    public FarmService(FarmRepository farmRepository) {
        this.farmRepository = farmRepository;
    }


    public Farm save(Farm farm) {
        return farmRepository.save(farm);
    }

    public Optional<Farm> findById(UUID id) {
        return farmRepository.findById(id);
    }

    public List<Farm> findAll() {
        return farmRepository.findAll();
    }

    public void delete(Farm farm) {
         farmRepository.delete(farm);
    }

    public List<Farm> searchFarms(String name, String location, LocalDate startDate) {
        return farmRepository.findAll(
                Specification
                        .where(FarmSpecification.nameContains(name))
                        .and(FarmSpecification.locationContains(location))
                        .and(FarmSpecification.creationDateAfter(startDate))
        );
    }
}
