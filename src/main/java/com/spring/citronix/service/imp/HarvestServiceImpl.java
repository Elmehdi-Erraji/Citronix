package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.enums.Season;

import com.spring.citronix.repository.HarvestRepository;
import com.spring.citronix.service.HarvestService;
import com.spring.citronix.web.errors.harvest.SeasonConflictException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HarvestServiceImpl implements HarvestService {
    private final HarvestRepository harvestRepository;

    public HarvestServiceImpl(HarvestRepository harvestRepository) {
        this.harvestRepository = harvestRepository;
    }

    @Override
    public Harvest save(@Valid Harvest harvest) {
        if (!isSeasonAvailable(harvest.getFarm().getId(), harvest.getSeason().toString())) {
            throw new SeasonConflictException("A harvest for this season already exists.");
        }
        return harvestRepository.save(harvest);
    }

    @Override
    public Optional<Harvest> findById(UUID id) {
        return harvestRepository.findById(id);
    }

    @Override
    public List<Harvest> findAll() {
        return harvestRepository.findAll();
    }

    @Override
    public List<Harvest> findByFarmId(UUID farmId) {
        return harvestRepository.findByFarmId(farmId);
    }

    @Override
    public void delete(Harvest harvest) {
        harvestRepository.delete(harvest);
    }

    @Override
    public boolean isSeasonAvailable(UUID farmId, String season) {
        return harvestRepository.findByFarmIdAndSeason(farmId, Season.valueOf(season)).isEmpty();
    }

    @Override
    public List<Harvest> getHarvestsBySeason(Season season) {
        return harvestRepository.findBySeason(season);
    }
}
