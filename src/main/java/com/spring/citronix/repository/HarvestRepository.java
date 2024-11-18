package com.spring.citronix.repository;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HarvestRepository extends JpaRepository<Harvest, UUID> {
    List<Harvest> findByFarmId(UUID farmId);

    Optional<Harvest> findByFarmIdAndSeason(UUID farmId, Season season);
    List<Harvest> findBySeason(Season season);
}
