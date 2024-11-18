package com.spring.citronix.repository;

import com.spring.citronix.domain.HarvestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Integer> {

    // Method to find HarvestDetails by Harvest ID
    List<HarvestDetail> findByHarvestId(UUID harvestId);

    // Method to find HarvestDetails by Tree ID
    List<HarvestDetail> findByTreeId(UUID treeId);
}
