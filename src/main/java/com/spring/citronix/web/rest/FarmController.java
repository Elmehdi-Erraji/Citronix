package com.spring.citronix.web.rest;


import com.spring.citronix.domain.Farm;
import com.spring.citronix.service.FarmService;
import com.spring.citronix.web.vm.request.farm.FarmSearchVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<Farm> updateFarm(@PathVariable UUID id, @RequestBody Farm farm) {
        Optional<Farm> farm1 = farmService.findById(id);
        if(farmService.findById(id) != null) {
            farm.setId(id);
            farmService.save(farm);
        }
        return ResponseEntity.ok(farm);
    }

    @PostMapping("/search")
    public List<Farm> searchFarm(@RequestBody FarmSearchVM farmSearch) {
        System.out.println(farmSearch);
        return farmService.searchFarms(farmSearch.getName(), farmSearch.getLocation(), farmSearch.getDate());
    }

}
