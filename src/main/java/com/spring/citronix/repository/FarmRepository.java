package com.spring.citronix.repository;

import com.spring.citronix.domain.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {
    Farm findById(UUID id);
}