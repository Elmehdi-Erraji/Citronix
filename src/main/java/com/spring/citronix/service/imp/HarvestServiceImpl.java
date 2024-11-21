package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Field;
import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.HarvestDetail;
import com.spring.citronix.domain.Tree;
import com.spring.citronix.domain.enums.Season;

import com.spring.citronix.repository.HarvestDetailRepository;
import com.spring.citronix.repository.HarvestRepository;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.service.HarvestService;
import com.spring.citronix.service.TreeService;
import com.spring.citronix.web.errors.harvest.SeasonConflictException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HarvestServiceImpl implements HarvestService {

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private FieldService fieldService;  // Assuming FieldService is available to get field details

    @Autowired
    private TreeService treeService;  // Assuming TreeService is available to get tree details

    @Autowired
    private HarvestDetailRepository harvestDetailRepository;

    @Transactional
    public Harvest save(Harvest harvest) {

        // Validate that the farm's fields haven't been harvested in the same season
        validateFieldsForHarvest(harvest);

        // Validate that trees haven't been harvested more than once in the same season
        for (Field field : harvest.getFarm().getFields()) {
            if (field.getTrees() != null) {
                for (Tree tree : field.getTrees()) {
                    validateTreeForHarvest(tree, harvest.getSeason());
                }
            }
        }

        // Save the harvest entity first
        Harvest savedHarvest = harvestRepository.save(harvest);

        // Now, create HarvestDetail records for each tree in each field
        for (Field field : harvest.getFarm().getFields()) {
            if (field.getTrees() != null) {
                for (Tree tree : field.getTrees()) {
                    // Calculate the quantity (e.g., based on tree's productivity or other factors)
                    double quantity = tree.calculateProductivity();

                    // Create a new HarvestDetail for each tree
                    HarvestDetail harvestDetail = new HarvestDetail();
                    harvestDetail.setQuantity(quantity);
                    harvestDetail.setTree(tree);
                    harvestDetail.setHarvest(savedHarvest);

                    // Save the HarvestDetail entity to the database
                    harvestDetailRepository.save(harvestDetail);
                }
            }
        }

        return savedHarvest;
    }

    // Method to validate if any field has already been harvested in the same season
    private void validateFieldsForHarvest(Harvest harvest) {
        // For each field in the harvest, check if it has already been harvested this season
        for (Field field : harvest.getFarm().getFields()) {
            List<Harvest> existingHarvests = harvestRepository.findByFarmAndSeason(harvest.getFarm(), harvest.getSeason());
            for (Harvest existingHarvest : existingHarvests) {
                // If this field is already associated with another harvest in the same season, throw an exception
                if (existingHarvest.getFarm().getFields().contains(field)) {
                    throw new IllegalStateException("Field has already been harvested in this season.");
                }
            }
        }
    }

    // Method to validate if a tree has already been harvested in the same season
    private void validateTreeForHarvest(Tree tree, Season season) {
        // Fetch harvest details for the tree and season to check for double harvest
        List<HarvestDetail> existingHarvestDetails = harvestDetailRepository.findByTreeAndSeason(tree, season);
        if (!existingHarvestDetails.isEmpty()) {
            throw new IllegalStateException("Tree has already been harvested in this season.");
        }
    }

    // Method to validate that no harvest exists for this farm in the same season
    private void validateHarvestForFarm(Harvest harvest) {
        List<Harvest> existingHarvests = harvestRepository.findByFarmAndSeason(harvest.getFarm(), harvest.getSeason());
        if (!existingHarvests.isEmpty()) {
            throw new IllegalStateException("This farm has already been harvested in the same season.");
        }
    }

    // This method checks if a harvest already exists for a given farm and season combination.
    private void validateHarvestDetails(Harvest harvest) {
        // Check if this harvest already exists in the system for the same season.
        List<Harvest> existingHarvests = harvestRepository.findByFarmAndSeason(harvest.getFarm(), harvest.getSeason());
        if (!existingHarvests.isEmpty()) {
            throw new IllegalStateException("Harvest already exists for this farm in the selected season.");
        }
    }



    @Override
    public Optional<Harvest> findById(UUID id) {
        return harvestRepository.findById(id);
    }

    @Override
    public List<Harvest> findAll() {
        return harvestRepository.findAll();
    }

    @Override
    public List<Harvest> findByFarmId(UUID farmId) {
        return harvestRepository.findByFarmId(farmId);
    }

    @Override
    public void delete(Harvest harvest) {
        harvestRepository.delete(harvest);
    }

    @Override
    public boolean isSeasonAvailable(UUID farmId, String season) {
        return harvestRepository.findByFarmIdAndSeason(farmId, Season.valueOf(season)).isEmpty();
    }

    @Override
    public List<Harvest> getHarvestsBySeason(Season season) {
        return harvestRepository.findBySeason(season);
    }
}
