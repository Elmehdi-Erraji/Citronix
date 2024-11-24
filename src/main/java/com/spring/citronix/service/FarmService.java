package com.spring.citronix.service;

import com.spring.citronix.domain.Farm;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FarmService {

    Farm save(Farm farm);
    Optional<Farm> findById(UUID id);
    Page<Farm> findAll(Pageable pageable);
    void delete(Farm farm);
    List<Farm> searchFarms(String name, String location, LocalDate startDate);

}
