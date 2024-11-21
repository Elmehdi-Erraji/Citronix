package com.spring.citronix.repository;

import com.spring.citronix.domain.HarvestDetail;
import com.spring.citronix.domain.Tree;
import com.spring.citronix.domain.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Integer> {

    // Method to find HarvestDetails by Harvest ID
    List<HarvestDetail> findByHarvestId(UUID harvestId);

    // Method to find HarvestDetails by Tree ID
    List<HarvestDetail> findByTreeId(UUID treeId);

    @Query("SELECT hd FROM HarvestDetail hd WHERE hd.harvest.season = :season AND hd.tree = :tree")
    List<HarvestDetail> findByTreeAndSeason(@Param("tree") Tree tree, @Param("season") Season season);
}
