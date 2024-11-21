package com.spring.citronix.web.rest;


import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;
import com.spring.citronix.service.FarmService;
import com.spring.citronix.web.mapper.request.FarmMapper;
import com.spring.citronix.web.vm.request.farm.FarmDTO;
import com.spring.citronix.web.vm.request.farm.FarmSearchVM;
import com.spring.citronix.web.vm.request.farm.FarmUpdateVM;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

            farm.setFields(fields);
        }

        return farmService.saveFromDTO(farm);
    }


    @GetMapping("/findById/{id}")
    public Optional<Farm> findById(@PathVariable UUID id) {
        return farmService.findById(id);
    }

    @GetMapping("/findAll")
    public List<Farm> findAll() {
        return farmService.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFarm(@PathVariable UUID id) {
        Optional<Farm> farm = farmService.findById(id);
        if (farm.isPresent()) {
            farmService.delete(farm.get());
            return ResponseEntity.ok("Farm deleted");
        }

        return ResponseEntity.ok("Farm not deleted");
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
    public List<Farm> searchFarm(@RequestBody FarmSearchVM farmSearch) {
        System.out.println(farmSearch);
        return farmService.searchFarms(farmSearch.getName(), farmSearch.getLocation(), farmSearch.getDate());
    }


    @GetMapping("/farm4000")
    public List<Farm> getFarms4000(){
        List<Farm> farms = farmService.getFarmsWithAreaLessThan4000();

        if (farms != null) {
            return farms;
        }else {
            return null;
        }
    }

}
