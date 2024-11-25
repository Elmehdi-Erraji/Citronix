package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Field;
import com.spring.citronix.domain.HarvestDetail;
import com.spring.citronix.domain.Tree;
import com.spring.citronix.repository.TreeRepository;
import com.spring.citronix.service.TreeService;
import com.spring.citronix.web.errors.TreeNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TreeServiceImpl implements TreeService {

    private static final int MAX_TREE_DENSITY_PER_HECTARE = 100;
    private final TreeRepository treeRepository;
    private final HarvestDetailService harvestDetailService;

    public TreeServiceImpl(TreeRepository treeRepository, HarvestDetailService harvestDetailService) {
        this.treeRepository = treeRepository;
        this.harvestDetailService = harvestDetailService;
    }

    @Override
    public Tree save(@Valid Tree tree) {
        validateTree(tree);
        tree.validatePlantingDate();
        return treeRepository.save(tree);
    }

    @Override
    public Optional<Tree> findById(UUID id) {
        return treeRepository.findById(id);
    }

    @Override
    public List<Tree> findAll() {
        return treeRepository.findAll();
    }

    @Override
    public Page<Tree> findByFieldId(UUID fieldId, Pageable pageable) {
        return treeRepository.findByFieldId(fieldId, pageable);
    }

    @Override
    public List<Tree> findByFieldId(UUID id) {
        return treeRepository.findByFieldId(id);
    }

    @Override
    public Page<Tree> findAll(Pageable pageable) {
        return treeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public void delete(UUID treeId) {
        Optional<Tree> tree = findById(treeId);
        if (tree.isPresent()) {
            List<HarvestDetail> harvestDetails = harvestDetailService.findByTreeId(treeId);
            for (HarvestDetail detail : harvestDetails) {
                harvestDetailService.delete(detail.getId());
            }

            treeRepository.delete(tree.get());
        } else {
            throw new EntityNotFoundException("Tree not found with ID: " + treeId);
        }
    }

    @Override
    public double calculateProductivity(UUID treeId) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new TreeNotFoundException("Tree not found with ID: " + treeId));
        return tree.calculateProductivity();
    }

    private void validateTree(Tree tree) {
        Field field = tree.getField();

        List<Tree> existingTrees = treeRepository.findByFieldId(field.getId());

        double fieldAreaInHectares = field.getArea() / 10000.0;

        int maxAllowedTrees = (int) Math.floor(fieldAreaInHectares * MAX_TREE_DENSITY_PER_HECTARE);

        if (existingTrees.size() >= maxAllowedTrees) {
            throw new IllegalArgumentException("Tree density exceeds " + MAX_TREE_DENSITY_PER_HECTARE +
                    " trees per hectare for field: " + field.getId() + ". Max allowed: " + maxAllowedTrees +
                    " for field area: " + field.getArea() + " m².");
        }
        if (tree.calculateAge() > 20) {
            throw new IllegalArgumentException("Tree is beyond its productive age (20 years) and cannot be added.");
        }
    }
}
