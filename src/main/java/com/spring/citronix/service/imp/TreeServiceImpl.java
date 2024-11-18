package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Tree;
import com.spring.citronix.repository.TreeRepository;
import com.spring.citronix.service.TreeService;
import com.spring.citronix.web.errors.tree.TreeNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TreeServiceImpl implements TreeService {

    private final TreeRepository treeRepository;

    public TreeServiceImpl(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }


    @Override
    public Tree save(@Valid Tree tree) {
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
    public List<Tree> findByFieldId(UUID fieldId) {
        return treeRepository.findByFieldId(fieldId);
    }

    @Override
    public void delete(Tree tree) {
        treeRepository.delete(tree);
    }

    @Override
    public double calculateProductivity(UUID treeId) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new TreeNotFoundException("Tree not found with ID: " + treeId));
        return tree.calculateProductivity();
    }
}
