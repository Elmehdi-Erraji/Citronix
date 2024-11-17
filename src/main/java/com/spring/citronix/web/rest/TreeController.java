package com.spring.citronix.web.rest;

import com.spring.citronix.domain.Tree;
import com.spring.citronix.service.TreeService;
import com.spring.citronix.web.errors.tree.TreeNotFoundException;

import com.spring.citronix.web.mapper.request.TreeMapper;
import com.spring.citronix.web.vm.request.tree.TreeCreateVM;
import com.spring.citronix.web.vm.response.tree.TreeResponseVM;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trees")
public class TreeController {
    private final TreeService treeService;
    private final TreeMapper treeMapper;

    public TreeController(TreeService treeService, TreeMapper treeMapper) {
        this.treeService = treeService;
        this.treeMapper = treeMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<TreeResponseVM> createTree(@Valid @RequestBody TreeCreateVM treeCreateVM) {
        Tree tree = treeMapper.toEntity(treeCreateVM);
        Tree savedTree = treeService.save(tree);
        return ResponseEntity.ok(treeMapper.toResponseVM(savedTree));
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<TreeResponseVM> getTree(@PathVariable UUID id) {
        Tree tree = treeService.findById(id)
                .orElseThrow(() -> new TreeNotFoundException("Tree not found with ID: " + id));
        return ResponseEntity.ok(treeMapper.toResponseVM(tree));
    }

    @GetMapping("/field/{fieldId}")
    public ResponseEntity<List<TreeResponseVM>> getTreesByField(@PathVariable UUID fieldId) {
        List<Tree> trees = treeService.findByFieldId(fieldId);
        return ResponseEntity.ok(trees.stream().map(treeMapper::toResponseVM).toList());
    }

    @GetMapping("/{id}/productivity")
    public ResponseEntity<Double> getTreeProductivity(@PathVariable UUID id) {
        double productivity = treeService.calculateProductivity(id);
        return ResponseEntity.ok(productivity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTree(@PathVariable UUID id) {
        Tree tree = treeService.findById(id)
                .orElseThrow(() -> new TreeNotFoundException("Tree not found with ID: " + id));
        treeService.delete(tree);
        return ResponseEntity.ok("Tree deleted.");
    }
}
