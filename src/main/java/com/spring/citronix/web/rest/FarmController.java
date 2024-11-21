package com.spring.citronix.web.rest;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;
import com.spring.citronix.service.FarmService;
import com.spring.citronix.web.mapper.request.FarmMapper;
import com.spring.citronix.web.vm.request.farm.FarmSearchVM;
import com.spring.citronix.web.vm.request.farm.FarmUpdateVM;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(summary = "Save a new farm", description = "Creates a new farm and stores it in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Farm created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Find a farm by ID", description = "Retrieves a farm based on the provided farm ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farm found successfully"),
            @ApiResponse(responseCode = "404", description = "Farm not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Farm> findById(

            @PathVariable UUID id) {
        return farmService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findAll")
    @Operation(summary = "Get all farms", description = "Retrieves a list of all farms with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farms retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Page<Farm> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return farmService.findAll(pageable);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a farm", description = "Deletes a farm by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farm deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Farm not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> deleteFarm(

            @PathVariable UUID id) {
        Optional<Farm> farm = farmService.findById(id);
        if (farm.isPresent()) {
            farmService.delete(farm.get());
            return ResponseEntity.ok("Farm deleted successfully.");
        }
        return ResponseEntity.status(404).body("Farm not found.");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a farm", description = "Updates farm details based on the provided ID and updated data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farm updated successfully"),
            @ApiResponse(responseCode = "404", description = "Farm not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Farm> updateFarm(

            @PathVariable UUID id,
            @RequestBody FarmUpdateVM farmUpdateVM) {
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
    @Operation(summary = "Search farms", description = "Search for farms by name, location, or date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farms retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Farm>> searchFarm(
          @RequestBody @Valid FarmSearchVM farmSearch) {
        List<Farm> farms = farmService.searchFarms(
                farmSearch.getName(),
                farmSearch.getLocation(),
                farmSearch.getDate()
        );
        return ResponseEntity.ok(farms);
    }
}
