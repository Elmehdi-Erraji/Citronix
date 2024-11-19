package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.repository.FarmRepository;
import com.spring.citronix.service.FarmService;
import com.spring.citronix.web.vm.request.farm.FarmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FarmServiceWithNoFields implements FarmService {
    private final FarmRepository farmRepository;

    @Override
    public Farm save(Farm farm) {
        if (farm.getFields() != null && !farm.getFields().isEmpty()) {
            throw new IllegalArgumentException("Fields are not allowed in this service implementation.");
        }
        return farmRepository.save(farm);
    }

    @Override
    public Farm saveFromDTO(Farm farm) {
        return null;
    }



    @Override
    public Optional<Farm> findById(UUID id) {
        return farmRepository.findById(id);
    }

    @Override
    public List<Farm> findAll() {
        return (List<Farm>) farmRepository.findAll();
    }

    @Override
    public void delete(Farm farm) {
        Optional<Farm> existingFarm = farmRepository.findById(farm.getId());
        if (existingFarm.isEmpty()) {
            throw new IllegalArgumentException("Farm not found.");
        }

        farm.setFields(null);
        farmRepository.delete(farm);
    }

    @Override
    public List<Farm> getFarmsWithAreaLessThan4000() {
        return List.of();
    }

    @Override
    public List<Farm> searchFarms(String name, String location, LocalDate startDate) {
        return List.of();
    }


}
