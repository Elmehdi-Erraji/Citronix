package com.spring.citronix.service;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.enums.Season;
import com.spring.citronix.web.vm.request.harvest.HarvestRequestVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HarvestService {
    Harvest harvestField(LocalDate harvetsDate, UUID fields);
    Harvest harvestFarm(LocalDate harvetsDate, UUID fields);

    Harvest getHarvestById(UUID harvestId);
    Page<Harvest> getAllHarvests(Season season, Integer year, Pageable pageable);
    Harvest updateHarvest(UUID harvestId, HarvestRequestVM request);
    void deleteHarvest(UUID harvestId);
}
