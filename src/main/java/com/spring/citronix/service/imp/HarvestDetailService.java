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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class HarvestDetailService {

    private final HarvestDetailRepository harvestDetailRepository;
    private final TreeRepository treeRepository;

    public HarvestDetailService(HarvestDetailRepository harvestDetailRepository, TreeRepository treeRepository) {
        this.harvestDetailRepository = harvestDetailRepository;
        this.treeRepository = treeRepository;
    }

    // Fetch HarvestDetails for a given Field
    public List<HarvestDetail> findByFieldId(UUID fieldId) {
        // Get trees that belong to the specified field
        List<Tree> trees = treeRepository.findByFieldId(fieldId);

        // Retrieve all HarvestDetails associated with these trees
        List<HarvestDetail> harvestDetails = new ArrayList<>();
        for (Tree tree : trees) {
            harvestDetails.addAll(harvestDetailRepository.findByTree(tree));
        }

        return harvestDetails;
    }

    // Delete HarvestDetail by its ID
    public void delete(UUID harvestDetailId) {
        HarvestDetail harvestDetail = (HarvestDetail) harvestDetailRepository.findById(harvestDetailId)
                .orElseThrow(() -> new IllegalArgumentException("HarvestDetail not found"));

        // Get the harvest related to this detail
        Harvest harvest = harvestDetail.getHarvest();

        // Remove the harvest detail from the harvest's list of details
        harvest.getHarvestDetails().remove(harvestDetail);

        // Delete the harvest detail
        harvestDetailRepository.deleteById(harvestDetail.getId());
    }

    // Find HarvestDetails by Tree ID
    public List<HarvestDetail> findByTreeId(UUID treeId) {
        return harvestDetailRepository.findByTreeId(treeId);
    }
}
