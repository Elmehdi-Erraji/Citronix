package com.spring.citronix.repository;

import com.spring.citronix.domain.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TreeRepository extends JpaRepository<Tree, UUID> {
    List<Tree> findByFieldId(UUID fieldId);
}
