package com.spring.citronix.repository;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;
import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.enums.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HarvestRepository extends JpaRepository<Harvest, UUID> {


    @Query("SELECT COUNT(h) > 0 FROM Harvest h JOIN h.harvestDetails hd JOIN hd.tree t WHERE t.field.id = :fieldId AND h.season = :season")
    boolean existsByFieldIdAndSeason(@Param("fieldId") UUID fieldId, @Param("season") Season season);

    @Query("SELECT h FROM Harvest h WHERE " +
            "(:season IS NULL OR h.season = :season) AND " +
            "(:year IS NULL OR YEAR(h.harvestDate) = :year)")
    Page<Harvest> findAllBySeasonAndYear(
            @Param("season") Season season,
            @Param("year") Integer year,
            Pageable pageable
    );

    @Query("SELECT COUNT(hd) > 0 FROM HarvestDetail hd WHERE hd.tree.id = :treeId AND hd.harvest.season = :season")
    boolean existsByTreeIdAndHarvestSeason(@Param("treeId") UUID treeId, @Param("season") Season season);


}
