package com.spring.citronix.web.rest;

import com.spring.citronix.domain.Field;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.web.errors.field.ResourceNotFoundException;
import com.spring.citronix.web.mapper.request.FieldMapper;
import com.spring.citronix.web.vm.request.field.FieldCreateVM;
import com.spring.citronix.web.vm.request.field.TreeDensityValidationVM;
import org.hibernate.validator.cfg.defs.UUIDDef;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fields")
public class FieldController {

    private final FieldService fieldService;
    private final FieldMapper fieldMapper;

    public FieldController(FieldService fieldService, FieldMapper fieldMapper) {
        this.fieldService = fieldService;
        this.fieldMapper = fieldMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<Field> createField(@Valid @RequestBody FieldCreateVM fieldCreateVM) {
        Field field = fieldMapper.toEntity(fieldCreateVM);
        Field savedField = fieldService.save(field);
        return ResponseEntity.ok(savedField);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Field> updateField(
            @PathVariable UUID id,
            @Valid @RequestBody FieldCreateVM fieldCreateVM) {

        Field existingField = fieldService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with ID: " + id));

        fieldMapper.updateEntityFromVM(fieldCreateVM, existingField);
        Field updatedField = fieldService.save(existingField);

        return ResponseEntity.ok(updatedField);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteField(@PathVariable UUID id) {
        fieldService.delete(id);
        return ResponseEntity.ok("Deleted field with id " + id);
    }

    @GetMapping("/farm/{farmId}")
    public ResponseEntity<List<Field>> getFieldsByFarmId(@PathVariable UUID farmId) {
        List<Field> fields = fieldService.findByFarmId(farmId);
        return ResponseEntity.ok(fields);
    }

}
