package com.spring.citronix.web.rest;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.enums.Season;
import com.spring.citronix.service.HarvestService;
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

    public HarvestController(HarvestService harvestService, HarvestMapper harvestMapper) {
        this.harvestService = harvestService;
        this.harvestMapper = harvestMapper;
    }

    @PostMapping
    public ResponseEntity<HarvestResponseVM> createHarvest(@Valid @RequestBody HarvestCreateVM harvestCreateVM) {
        Harvest harvest = harvestMapper.toEntity(harvestCreateVM);
        Harvest savedHarvest = harvestService.save(harvest);
        return ResponseEntity.ok(harvestMapper.toResponseVM(savedHarvest));
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
