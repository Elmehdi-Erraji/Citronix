package com.spring.citronix.service;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.web.vm.request.farm.FarmDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface FarmService {

    Farm save(Farm farm);

    Farm saveFromDTO(Farm farm);

    Optional<Farm> findById(UUID id);

    List<Farm> findAll();

    void delete(Farm farm);
    List<Farm> getFarmsWithAreaLessThan4000();
    List<Farm> searchFarms(String name, String location, LocalDate startDate);
}
