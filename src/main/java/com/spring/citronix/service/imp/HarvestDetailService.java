package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.HarvestDetail;
import com.spring.citronix.domain.Tree;
import com.spring.citronix.repository.HarvestDetailRepository;
import com.spring.citronix.repository.HarvestRepository;
import com.spring.citronix.repository.TreeRepository;

import com.spring.citronix.web.vm.request.harvest.HarvestDetailCreateVM;
import com.spring.citronix.web.vm.response.harvest.HarvestDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class HarvestDetailService {

    private final HarvestDetailRepository harvestDetailRepository;
    private final HarvestRepository harvestRepository;
    private final TreeRepository treeRepository;

    public HarvestDetailService(HarvestDetailRepository harvestDetailRepository, HarvestRepository harvestRepository, TreeRepository treeRepository) {
        this.harvestDetailRepository = harvestDetailRepository;
        this.harvestRepository = harvestRepository;
        this.treeRepository = treeRepository;
    }


    /**
     * Create a new HarvestDetail and associate it with a specific Harvest and Tree.
     */
    public HarvestDetailResponse createHarvestDetail(HarvestDetailCreateVM request) {
        // Fetch Harvest and Tree from their respective repositories
        Harvest harvest = harvestRepository.findById(request.getHarvestId())
                .orElseThrow(() -> new IllegalArgumentException("Harvest not found with ID: " + request.getHarvestId()));

        Tree tree = treeRepository.findById(request.getTreeId())
                .orElseThrow(() -> new IllegalArgumentException("Tree not found with ID: " + request.getTreeId()));

        // Create and save the HarvestDetail
        HarvestDetail harvestDetail = new HarvestDetail();
        harvestDetail.setHarvest(harvest);
        harvestDetail.setTree(tree);
        harvestDetail.setQuantity(request.getQuantity());

        HarvestDetail savedDetail = harvestDetailRepository.save(harvestDetail);

        // Return a response
        return new HarvestDetailResponse(
                savedDetail.getId(),
                savedDetail.getHarvest().getId(),
                savedDetail.getTree().getId(),
                savedDetail.getQuantity()
        );
    }

    /**
     * Get all HarvestDetails by Harvest ID.
     */
    public List<HarvestDetail> getHarvestDetailsByHarvestId(UUID harvestId) {
        return harvestDetailRepository.findByHarvestId(harvestId);
    }

    /**
     * Get all HarvestDetails by Tree ID.
     */
    public List<HarvestDetail> getHarvestDetailsByTreeId(UUID treeId) {
        return harvestDetailRepository.findByTreeId(treeId);
    }
}
