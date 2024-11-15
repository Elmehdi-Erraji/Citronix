package com.spring.citronix.service;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.repository.FarmRepository;
import org.springframework.stereotype.Service;

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

    public Farm findById(UUID id) {
        return farmRepository.findById(id);
    }

    public void delete(Farm farm) {
         farmRepository.delete(farm);
    }
}
