package com.spring.citronix;

import com.spring.citronix.domain.*;
import com.spring.citronix.repository.*;
import com.spring.citronix.service.*;
import com.spring.citronix.service.imp.*;
import com.spring.citronix.web.vm.request.harvest.HarvestRequestVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HarvestServiceImplTest {

    @InjectMocks
    private HarvestServiceImpl harvestService;

    @Mock
    private FieldService fieldService;

    @Mock
    private TreeService treeService;

    @Mock
    private HarvestRepository harvestRepository;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private TreeRepository treeRepository;

    @Mock
    private HarvestDetailRepository harvestDetailRepository;

    @Mock
    private HarvestDetailService harvestDetailService;

    @Mock
    private SalesService salesService;




    @Test
    void testHarvestField_FieldNotFound() {
        UUID fieldId = UUID.randomUUID();
        LocalDate harvestDate = LocalDate.now();

        when(fieldService.findById(fieldId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> harvestService.harvestField(harvestDate, fieldId));

        assertEquals("Field not found with id: " + fieldId, exception.getMessage());
    }


    @Test
    void testGetHarvestById_Success() {
        UUID harvestId = UUID.randomUUID();
        Harvest harvest = new Harvest();
        harvest.setId(harvestId);

        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(harvest));

        Harvest result = harvestService.getHarvestById(harvestId);

        assertEquals(harvestId, result.getId());
    }

    @Test
    void testGetHarvestById_NotFound() {
        UUID harvestId = UUID.randomUUID();

        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> harvestService.getHarvestById(harvestId));

        assertEquals("Harvest not found with id: " + harvestId, exception.getMessage());
    }

    @Test
    void testUpdateHarvest_NotSupported() {
        UUID harvestId = UUID.randomUUID();
        HarvestRequestVM request = new HarvestRequestVM();

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> harvestService.updateHarvest(harvestId, request));

        assertEquals("harvest updated", exception.getMessage());
    }

    @Test
    void testDeleteHarvest_Success() {
        UUID harvestId = UUID.randomUUID();
        Harvest harvest = new Harvest();
        harvest.setId(harvestId);
        harvest.setSales(new ArrayList<>());
        harvest.setHarvestDetails(new ArrayList<>());

        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(harvest));

        harvestService.deleteHarvest(harvestId);

        verify(harvestRepository).delete(harvest);
    }

    @Test
    void testDeleteHarvest_NotFound() {
        UUID harvestId = UUID.randomUUID();

        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> harvestService.deleteHarvest(harvestId));

        assertEquals("Harvest not found", exception.getMessage());
    }
}
