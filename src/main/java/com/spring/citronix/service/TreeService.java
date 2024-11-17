package com.spring.citronix.service;

import com.spring.citronix.domain.Tree;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreeService {

    Tree save(Tree tree);

    Optional<Tree> findById(UUID id);

    List<Tree> findAll();

    List<Tree> findByFieldId(UUID fieldId);

    void delete(Tree tree);

    double calculateProductivity(UUID treeId);
}
