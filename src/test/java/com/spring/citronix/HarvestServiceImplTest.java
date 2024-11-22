package com.spring.citronix;

import com.spring.citronix.domain.*;
import com.spring.citronix.domain.enums.Season;
import com.spring.citronix.repository.HarvestDetailRepository;
import com.spring.citronix.repository.HarvestRepository;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.service.HarvestService;
import com.spring.citronix.service.TreeService;
import com.spring.citronix.service.imp.HarvestServiceImpl;
import com.spring.citronix.web.errors.HarvestNotFoundException;
import com.spring.citronix.web.vm.request.harvest.HarvestRequestVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class HarvestServiceImplTest {

    @Mock
    private FieldService fieldService;

    @Mock
    private TreeService treeService;

    @Mock
    private HarvestRepository harvestRepository;

    @Mock
    private HarvestDetailRepository harvestDetailRepository;

    @InjectMocks
    private HarvestServiceImpl harvestService;

    private UUID fieldId;
    private UUID treeId;
    private Field field;
    private Tree tree;

    @BeforeEach
    void setUp() {
        fieldId = UUID.randomUUID();
        treeId = UUID.randomUUID();
        field = new Field();
        field.setId(fieldId);

        tree = new Tree();
        tree.setId(treeId);
        tree.setField(field);
    }


    @Test
    void testHarvestField_InvalidYear() {
        LocalDate harvestDate = LocalDate.of(2023, 8, 15);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                harvestService.harvestField(harvestDate, fieldId));
        assertEquals("Harvest year must be the current year.", exception.getMessage());
    }

    @Test
    void testHarvestField_FieldNotFound() {
        LocalDate harvestDate = LocalDate.now();
        when(fieldService.findById(fieldId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                harvestService.harvestField(harvestDate, fieldId));
        assertEquals("Field not found with id: " + fieldId, exception.getMessage());
    }



    @Test
    void testHarvestField_EmptyTreesInField() {
        LocalDate harvestDate = LocalDate.now();
        when(fieldService.findById(fieldId)).thenReturn(Optional.of(field));
        when(treeService.findByFieldId(fieldId, Pageable.unpaged())).thenReturn(Page.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                harvestService.harvestField(harvestDate, fieldId));
        assertEquals("No trees available in the field for harvesting.", exception.getMessage());
    }




    @Test
    void testGetHarvestById_NotFound() {
        UUID harvestId = UUID.randomUUID();
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                harvestService.getHarvestById(harvestId));
        assertEquals("Harvest not found with id: " + harvestId, exception.getMessage());
    }

    @Test
    void testDeleteHarvest_Success() {
        UUID harvestId = UUID.randomUUID();
        when(harvestRepository.existsById(harvestId)).thenReturn(true);
        harvestService.deleteHarvest(harvestId);
        verify(harvestRepository, times(1)).deleteById(harvestId);
    }



}
