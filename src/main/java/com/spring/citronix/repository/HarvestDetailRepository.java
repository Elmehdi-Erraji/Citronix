package com.spring.citronix.repository;

import com.spring.citronix.domain.HarvestDetail;
import com.spring.citronix.domain.Tree;
import com.spring.citronix.domain.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Integer> {

    boolean existsByTreeIdAndHarvestSeason(UUID treeId, Season season);
    void deleteById(UUID id);
    Optional<Object> findById(UUID harvestDetailId);
    List<HarvestDetail> findByTreeId(UUID treeId);
    List<HarvestDetail> findByTree(Tree tree);

}
