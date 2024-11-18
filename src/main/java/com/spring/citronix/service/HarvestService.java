package com.spring.citronix.service;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.enums.Season;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HarvestService {
    Harvest save(Harvest harvest);

    Optional<Harvest> findById(UUID id);

    List<Harvest> findAll();

    List<Harvest> findByFarmId(UUID farmId);

    void delete(Harvest harvest);

    boolean isSeasonAvailable(UUID farmId, String season);

    List<Harvest> getHarvestsBySeason(Season season);
}
