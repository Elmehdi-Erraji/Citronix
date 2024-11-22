package com.spring.citronix.web.rest;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;
import com.spring.citronix.service.FarmService;
import com.spring.citronix.web.mapper.request.FarmMapper;
import com.spring.citronix.web.vm.request.farm.FarmCreateVM;
import com.spring.citronix.web.vm.request.farm.FarmSearchVM;
import com.spring.citronix.web.vm.request.farm.FarmUpdateVM;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/farm")
public class FarmController {

    private final FarmService farmService;
    private final FarmMapper farmMapper;

    public FarmController(FarmService farmService, FarmMapper farmMapper) {
        this.farmService = farmService;
        this.farmMapper = farmMapper;
    }

    @PostMapping("/create")
    public Farm createFarm(@RequestBody @Valid Farm farmDTO) {
        if (farmDTO.getFields() != null) {
            List<Field> fields = farmDTO.getFields().stream().map(listField -> {
                Field field = new Field();
                field.setArea(listField.getArea());
                return field;
            }).collect(Collectors.toList());
            farmDTO.setFields(fields);
        }
        return farmService.save(farmDTO);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Farm> findById(@PathVariable UUID id) {
        return farmService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findAll")
    public Page<FarmCreateVM> findAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Farm> farmsPage = farmService.findAll(pageable);

        List<FarmCreateVM> farmCreateVMList = farmsPage.getContent().stream()
                .map(farmMapper::toCreateVM)
                .collect(Collectors.toList());

        return new PageImpl<>(farmCreateVMList, pageable, farmsPage.getTotalElements());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFarm( @PathVariable UUID id) {
        Optional<Farm> farm = farmService.findById(id);
        if (farm.isPresent()) {
            farmService.delete(farm.get());
            return ResponseEntity.ok("Farm deleted successfully.");
        }
        return ResponseEntity.status(404).body("Farm not found.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Farm> updateFarm(@PathVariable UUID id, @RequestBody FarmUpdateVM farmUpdateVM) {
        Optional<Farm> existingFarmOpt = farmService.findById(id);
        if (!existingFarmOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Farm existingFarm = existingFarmOpt.get();
        Farm updatedFarm = farmMapper.toEntity(farmUpdateVM);
        updatedFarm.setId(id);
        Farm savedFarm = farmService.save(updatedFarm);
        return ResponseEntity.ok(savedFarm);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchFarm(@RequestBody @Valid FarmSearchVM farmSearch) {
        List<Farm> farms = farmService.searchFarms(
                farmSearch.getName(),
                farmSearch.getLocation(),
                farmSearch.getDate()
        );
        if (farms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No farms found");
        }

        List<FarmCreateVM> farmCreateVMList = farms.stream()
                .map(farmMapper::toCreateVM)
                .collect(Collectors.toList());

        return ResponseEntity.ok(farmCreateVMList);
    }
}
