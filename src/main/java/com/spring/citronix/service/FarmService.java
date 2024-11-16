package com.spring.citronix.service;

import com.spring.citronix.domain.Farm;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface FarmService {

    Farm save(Farm farm);

    Optional<Farm> findById(UUID id);

    List<Farm> findAll();

    void delete(Farm farm);

    List<Farm> searchFarms(String name, String location, LocalDate startDate);
}
