package com.spring.citronix.service.imp;

import com.spring.citronix.domain.*;
import com.spring.citronix.domain.enums.Season;
import com.spring.citronix.repository.HarvestDetailRepository;
import com.spring.citronix.repository.HarvestRepository;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.service.HarvestService;
import com.spring.citronix.service.SalesService;
import com.spring.citronix.service.TreeService;
import com.spring.citronix.web.vm.request.harvest.HarvestRequestVM;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HarvestServiceImpl implements HarvestService {

    private final FieldService fieldService;
    private final TreeService treeService;
    private final HarvestRepository harvestRepository;
    private final HarvestDetailRepository harvestDetailRepository;
    private final HarvestDetailService harvestDetailService;
    private final SalesService salesService;



    @Override
    public Harvest harvestField(LocalDate harvestDate, UUID fieldId) {
        validateHarvestYear(harvestDate);
        Field field = validateFieldExistence(fieldId);
        validateFieldNotAlreadyHarvested(fieldId, harvestDate);
        List<Tree> trees = validateTreesInField(fieldId);
        List<HarvestDetail> harvestDetails = createHarvestDetailsForTrees(trees, harvestDate);

        if (harvestDetails.isEmpty()) {
            throw new IllegalArgumentException("No valid trees found for harvesting in the field.");
        }

        double totalQuantity = harvestDetails.stream()
                .mapToDouble(HarvestDetail::getQuantity)
                .sum();

        Harvest harvest = new Harvest();
        harvest.setHarvestDate(harvestDate);
        harvest.setSeason(determineSeason(harvestDate));
        harvest.setTotalQuantity(totalQuantity);
        harvest.setHarvestDetails(harvestDetails);

        Harvest savedHarvest = harvestRepository.save(harvest);
        saveHarvestDetails(savedHarvest, harvestDetails);

        return savedHarvest;
    }

    private void validateHarvestYear(LocalDate harvestDate) {
        int currentYear = LocalDate.now().getYear();
        if (harvestDate.getYear() != currentYear) {
            throw new IllegalArgumentException("Harvest year must be the current year.");
        }
    }

    private Field validateFieldExistence(UUID fieldId) {
        return fieldService.findById(fieldId)
                .orElseThrow(() -> new IllegalArgumentException("Field not found with id: " + fieldId));
    }

    private void validateFieldNotAlreadyHarvested(UUID fieldId, LocalDate harvestDate) {
        Season currentSeason = determineSeason(harvestDate);
        boolean hasHarvest = harvestRepository.existsByFieldIdAndSeason(fieldId, currentSeason);
        if (hasHarvest) {
            throw new IllegalArgumentException("Field has already been harvested for this season.");
        }
    }

    private List<Tree> validateTreesInField(UUID fieldId) {
        List<Tree> trees = treeService.findByFieldId(fieldId, Pageable.unpaged()).getContent();
        if (trees.isEmpty()) {
            throw new IllegalArgumentException("No trees available in the field for harvesting.");
        }
        return trees;
    }

    private List<HarvestDetail> createHarvestDetailsForTrees(List<Tree> trees, LocalDate harvestDate) {
        List<HarvestDetail> harvestDetails = new ArrayList<>();
        Season currentSeason = determineSeason(harvestDate);

        for (Tree tree : trees) {
            if (tree.calculateAge() > 20 || hasTreeBeenHarvestedInSeason(tree.getId(), currentSeason)) continue;
            double treeProductivity = treeService.calculateProductivity(tree.getId());
            HarvestDetail detail = new HarvestDetail();
            detail.setTree(tree);
            detail.setQuantity(treeProductivity);
            harvestDetails.add(detail);
        }
        return harvestDetails;
    }

    private boolean hasTreeBeenHarvestedInSeason(UUID treeId, Season season) {
        return harvestDetailRepository.existsByTreeIdAndHarvestSeason(treeId, season);
    }

    private void saveHarvestDetails(Harvest savedHarvest, List<HarvestDetail> harvestDetails) {
        for (HarvestDetail detail : harvestDetails) {
            detail.setHarvest(savedHarvest);
            harvestDetailRepository.save(detail);
        }
    }

    private Season determineSeason(LocalDate harvestDate) {
        int month = harvestDate.getMonthValue();
        if (month >= 3 && month <= 5) {
            return Season.SPRING;
        } else if (month >= 6 && month <= 8) {
            return Season.SUMMER;
        } else if (month >= 9 && month <= 11) {
            return Season.FALL;
        } else {
            return Season.WINTER;
        }
    }

    @Override
    public Harvest harvestFarm(LocalDate harvestDate, UUID farmId) {
        validateHarvestYear(harvestDate);
        List<Field> fields = validateFieldsInFarm(farmId);

        double totalFarmQuantity = 0.0;
        List<HarvestDetail> allHarvestDetails = new ArrayList<>();
        List<String> skippedFields = new ArrayList<>();

        for (Field field : fields) {
            try {
                validateFieldNotAlreadyHarvested(field.getId(), harvestDate);
                List<Tree> trees = validateTreesInField(field.getId());
                List<HarvestDetail> harvestDetailsForField = createHarvestDetailsForTrees(trees, harvestDate);

                if (harvestDetailsForField.isEmpty()) {
                    skippedFields.add("Field with ID: " + field.getId() + " has no valid trees for harvesting.");
                    continue;
                }

                double totalQuantityForField = harvestDetailsForField.stream()
                        .mapToDouble(HarvestDetail::getQuantity)
                        .sum();
                totalFarmQuantity += totalQuantityForField;
                allHarvestDetails.addAll(harvestDetailsForField);
            } catch (Exception e) {
                skippedFields.add("Error processing field with ID: " + field.getId() + " - " + e.getMessage());
            }
        }

        if (allHarvestDetails.isEmpty()) {
            throw new IllegalArgumentException("No valid harvests were found for any of the fields.");
        }

        Harvest farmHarvest = new Harvest();
        farmHarvest.setHarvestDate(harvestDate);
        farmHarvest.setSeason(determineSeason(harvestDate));
        farmHarvest.setTotalQuantity(totalFarmQuantity);
        farmHarvest.setHarvestDetails(allHarvestDetails);

        Harvest savedFarmHarvest = harvestRepository.save(farmHarvest);
        saveHarvestDetails(savedFarmHarvest, allHarvestDetails);

        if (!skippedFields.isEmpty()) {
            skippedFields.forEach(System.out::println);
        }

        return savedFarmHarvest;
    }

    private List<Field> validateFieldsInFarm(UUID farmId) {
        List<Field> fields = fieldService.findByFarmId(farmId, Pageable.unpaged()).getContent();
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("No fields found for the farm with id: " + farmId);
        }
        return fields;
    }

    @Override
    public Harvest getHarvestById(UUID harvestId) {
        return harvestRepository.findById(harvestId)
                .orElseThrow(() -> new IllegalArgumentException("Harvest not found with id: " + harvestId));
    }

    @Override
    public Page<Harvest> getAllHarvests(Pageable pageable) {
        return harvestRepository.findAll(pageable);
    }

    @Override
    public Harvest updateHarvest(UUID harvestId, HarvestRequestVM request) {
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @Override
    public void deleteHarvest(UUID harvestId) {
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new IllegalArgumentException("Harvest not found"));

        for (Sale sale : harvest.getSales()) {
            salesService.deleteSale(sale.getId());
        }

        for (HarvestDetail detail : harvest.getHarvestDetails()) {
            harvestDetailService.delete(detail.getId());
        }

        harvestRepository.delete(harvest);
    }

}
