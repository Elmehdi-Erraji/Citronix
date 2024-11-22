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

    public List<HarvestDetail> findByFieldId(UUID fieldId) {
        List<Tree> trees = treeRepository.findByFieldId(fieldId);

        List<HarvestDetail> harvestDetails = new ArrayList<>();
        for (Tree tree : trees) {
            harvestDetails.addAll(harvestDetailRepository.findByTree(tree));
        }

        return harvestDetails;
    }

    public void delete(UUID harvestDetailId) {
        HarvestDetail harvestDetail = (HarvestDetail) harvestDetailRepository.findById(harvestDetailId)
                .orElseThrow(() -> new IllegalArgumentException("HarvestDetail not found"));

        Harvest harvest = harvestDetail.getHarvest();

        harvest.getHarvestDetails().remove(harvestDetail);

        harvestDetailRepository.deleteById(harvestDetail.getId());
    }


    public List<HarvestDetail> findByTreeId(UUID treeId) {
        return harvestDetailRepository.findByTreeId(treeId);
    }
}
