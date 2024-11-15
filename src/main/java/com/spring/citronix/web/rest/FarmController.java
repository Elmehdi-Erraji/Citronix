package com.spring.citronix.web.rest;


import com.spring.citronix.domain.Farm;
import com.spring.citronix.service.FarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/farm")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }


    @PostMapping("/create")
    public Farm createFarm(@RequestBody Farm farm) {
        return farmService.save(farm);
    }

    @GetMapping("/findById/{id}")
    public Farm findById(@PathVariable UUID id) {
        return farmService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFarm(@PathVariable UUID id) {
        Farm farm = farmService.findById(id);
        if(farmService.findById(id) != null) {
            farmService.delete(farm);
            return ResponseEntity.ok("Farm deleted");
        }
        return ResponseEntity.ok("Farm not deleted");
    }
}
