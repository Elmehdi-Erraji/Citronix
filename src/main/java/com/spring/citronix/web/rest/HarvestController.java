package com.spring.citronix.web.rest;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.enums.Season;
import com.spring.citronix.service.HarvestService;
import com.spring.citronix.service.imp.FarmServiceImp;
import com.spring.citronix.web.errors.harvest.HarvestNotFoundException;
import com.spring.citronix.web.mapper.request.HarvestMapper;

import com.spring.citronix.web.vm.request.harvest.HarvestCreateVM;
import com.spring.citronix.web.vm.response.harvest.HarvestResponseVM;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/harvests")
public class HarvestController {
    private final HarvestService harvestService;
    private final HarvestMapper harvestMapper;
    private final FarmServiceImp farmServiceImp;

    public HarvestController(HarvestService harvestService, HarvestMapper harvestMapper, FarmServiceImp farmServiceImp) {
        this.harvestService = harvestService;
        this.harvestMapper = harvestMapper;
        this.farmServiceImp = farmServiceImp;
    }

//    @PostMapping
//    public ResponseEntity<HarvestResponseVM> createHarvest(@Valid @RequestBody HarvestCreateVM harvestCreateVM) {
//        Harvest harvest = harvestMapper.toEntity(harvestCreateVM);
//        Harvest savedHarvest = harvestService.save(harvest);
//        return ResponseEntity.ok(harvestMapper.toResponseVM(savedHarvest));
//    }

    @PostMapping
    public ResponseEntity<Harvest> saveHarvest(@Valid @RequestBody HarvestCreateVM harvestDTO) {

        // Fetch the farm by its id
        Farm farm = farmServiceImp.findById(harvestDTO.getFarmId())
                .orElseThrow(() -> new IllegalArgumentException("Farm not found"));

        // Create a new Harvest instance and set its properties
        Harvest harvest = new Harvest();
        harvest.setSeason(Season.valueOf(harvestDTO.getSeason()));
        harvest.setHarvestDate(harvestDTO.getHarvestDate());
        harvest.setTotalQuantity(harvestDTO.getTotalQuantity());
        harvest.setFarm(farm);

        // Save the harvest and return the result
        Harvest savedHarvest = harvestService.save(harvest);

        return ResponseEntity.ok(savedHarvest);
    }










    @GetMapping("/{id}")
    public ResponseEntity<HarvestResponseVM> getHarvest(@PathVariable UUID id) {
        Harvest harvest = harvestService.findById(id)
                .orElseThrow(() -> new HarvestNotFoundException("Harvest not found with ID: " + id));
        return ResponseEntity.ok(harvestMapper.toResponseVM(harvest));
    }

    @GetMapping("/farm/{farmId}")
    public List<HarvestResponseVM> getHarvestsByFarm(@PathVariable UUID farmId) {
        return harvestService.findByFarmId(farmId)
                .stream()
                .map(harvestMapper::toResponseVM)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHarvest(@PathVariable UUID id) {
        Harvest harvest = harvestService.findById(id)
                .orElseThrow(() -> new HarvestNotFoundException("Harvest not found with ID: " + id));
        harvestService.delete(harvest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/season/{season}")
    public ResponseEntity<List<Harvest>> getHarvestsBySeason(@PathVariable Season season) {
        List<Harvest> harvests = harvestService.getHarvestsBySeason(season);
        return ResponseEntity.ok(harvests);
    }
}
