package com.spring.citronix;

import com.spring.citronix.domain.*;
import com.spring.citronix.domain.enums.Season;
import com.spring.citronix.repository.HarvestDetailRepository;
import com.spring.citronix.repository.HarvestRepository;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.service.SalesService;
import com.spring.citronix.service.TreeService;
import com.spring.citronix.service.imp.HarvestDetailService;
import com.spring.citronix.service.imp.HarvestServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HarvestServiceTest {

    @Mock private FieldService fieldService;
    @Mock private TreeService treeService;
    @Mock private HarvestRepository harvestRepository;
    @Mock private HarvestDetailRepository harvestDetailRepository;
    @Mock private HarvestDetailService harvestDetailService;
    @Mock private SalesService salesService;

    @InjectMocks private HarvestServiceImpl harvestService;

    @Test
    void testHarvestField_validHarvest() {
        // Given
        UUID fieldId = UUID.randomUUID();
        LocalDate harvestDate = LocalDate.of(2024, 5, 10); // Spring season
        Field field = mock(Field.class);
        List<Tree> trees = Arrays.asList(mock(Tree.class), mock(Tree.class)); // Mocked trees
        List<HarvestDetail> harvestDetails = Arrays.asList(mock(HarvestDetail.class), mock(HarvestDetail.class));

        when(fieldService.findById(fieldId)).thenReturn(Optional.of(field));
        when(treeService.findByFieldId(fieldId, Pageable.unpaged())).thenReturn(new PageImpl<>(trees));
        when(harvestRepository.save(any(Harvest.class))).thenReturn(mock(Harvest.class));

        // Simulate successful harvest creation
        when(harvestDetailRepository.save(any(HarvestDetail.class))).thenReturn(mock(HarvestDetail.class));

        // When
        Harvest result = harvestService.harvestField(harvestDate, fieldId);

        // Then
        assertNotNull(result);
        assertEquals(harvestDate, result.getHarvestDate());
        assertEquals(Season.SPRING, result.getSeason());
    }


    @Test
    void testHarvestFarm_validHarvest() {
        // Given
        UUID farmId = UUID.randomUUID();
        LocalDate harvestDate = LocalDate.of(2024, 5, 10); // Spring season
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        List<Field> fields = Arrays.asList(field1, field2);
        List<HarvestDetail> harvestDetails = Arrays.asList(mock(HarvestDetail.class), mock(HarvestDetail.class));

        when(fieldService.findByFarmId(farmId, Pageable.unpaged())).thenReturn(new PageImpl<>(fields));
        when(harvestRepository.save(any(Harvest.class))).thenReturn(mock(Harvest.class));
        when(treeService.findByFieldId(field1.getId(), Pageable.unpaged())).thenReturn(new PageImpl<>(Arrays.asList(mock(Tree.class))));
        when(treeService.findByFieldId(field2.getId(), Pageable.unpaged())).thenReturn(new PageImpl<>(Arrays.asList(mock(Tree.class))));
        when(harvestDetailRepository.save(any(HarvestDetail.class))).thenReturn(mock(HarvestDetail.class));

        // When
        Harvest result = harvestService.harvestFarm(harvestDate, farmId);

        // Then
        assertNotNull(result);
        assertEquals(harvestDate, result.getHarvestDate());
        assertEquals(Season.SPRING, result.getSeason());
    }


    @Test
    void testHarvestFarm_noValidTreesInField() {
        // Given
        UUID farmId = UUID.randomUUID();
        LocalDate harvestDate = LocalDate.of(2024, 5, 10); // Spring season
        Field field = mock(Field.class);
        List<Field> fields = Collections.singletonList(field);
        List<Tree> trees = Collections.emptyList(); // No trees for harvest

        when(fieldService.findByFarmId(farmId, Pageable.unpaged())).thenReturn(new PageImpl<>(fields));
        when(treeService.findByFieldId(field.getId(), Pageable.unpaged())).thenReturn(new PageImpl<>(trees));

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            harvestService.harvestFarm(harvestDate, farmId);
        });

        // Then
        assertEquals("No valid harvests were found for any of the fields.", exception.getMessage());
    }



    @Test
    void testGetHarvestById_notFound() {
        // Given
        UUID harvestId = UUID.randomUUID();

        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            harvestService.getHarvestById(harvestId);
        });

        // Then
        assertEquals("Harvest not found with id: " + harvestId, exception.getMessage());
    }

    @Test
    void testDetermineSeason_winter() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 1); // Winter season

        // When
        Season result = harvestService.determineSeason(date);

        // Then
        assertEquals(Season.WINTER, result);
    }

    @Test
    void testDetermineSeason_spring() {
        // Given
        LocalDate date = LocalDate.of(2024, 4, 1); // Spring season

        // When
        Season result = harvestService.determineSeason(date);

        // Then
        assertEquals(Season.SPRING, result);
    }

    @Test
    void testValidateFieldExistence_fieldNotFound() {
        // Given
        UUID fieldId = UUID.randomUUID();

        when(fieldService.findById(fieldId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            harvestService.validateFieldExistence(fieldId);
        });

        // Then
        assertEquals("Field not found with id: " + fieldId, exception.getMessage());
    }
}
