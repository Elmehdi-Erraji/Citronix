package com.spring.citronix.web.rest;

import com.spring.citronix.domain.HarvestDetail;
import com.spring.citronix.service.imp.HarvestDetailService;
import com.spring.citronix.web.vm.request.harvest.HarvestDetailCreateVM;
import com.spring.citronix.web.vm.response.harvest.HarvestDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/harvest-details")
public class HarvestDetailController {

    private final HarvestDetailService harvestDetailService;

    public HarvestDetailController(HarvestDetailService harvestDetailService) {
        this.harvestDetailService = harvestDetailService;
    }

    @PostMapping
    public ResponseEntity<HarvestDetailResponse> createHarvestDetail(@RequestBody HarvestDetailCreateVM request) {
        HarvestDetailResponse response = harvestDetailService.createHarvestDetail(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/harvest/{harvestId}")
    public ResponseEntity<List<HarvestDetail>> getHarvestDetailsByHarvestId(@PathVariable UUID harvestId) {
        return ResponseEntity.ok(harvestDetailService.getHarvestDetailsByHarvestId(harvestId));
    }

    @GetMapping("/tree/{treeId}")
    public ResponseEntity<List<HarvestDetail>> getHarvestDetailsByTreeId(@PathVariable UUID treeId) {
        return ResponseEntity.ok(harvestDetailService.getHarvestDetailsByTreeId(treeId));
    }
}
