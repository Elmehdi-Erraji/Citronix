package com.spring.citronix;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;
import com.spring.citronix.repository.FarmRepository;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.service.imp.FarmServiceImp;
import com.spring.citronix.web.errors.farm.FarmNotFoundException;
import com.spring.citronix.web.errors.farm.InvalidFarmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FarmServiceTest {

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FieldService fieldService;

    @InjectMocks
    private FarmServiceImp farmService;

    private Farm farm;
    private Field field;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup common objects for tests
        field = new Field();
        field.setArea(20);

        farm = new Farm();
        farm.setId(UUID.randomUUID());
        farm.setName("Farm 1");
        farm.setLocation("Location 1");
        farm.setArea(100);
        farm.setCreationDate(LocalDate.now());
        farm.setFields(Arrays.asList(field));
    }

    @Test
    void saveFarm_Valid() {
        when(farmRepository.save(farm)).thenReturn(farm);

        Farm savedFarm = farmService.save(farm);

        assertNotNull(savedFarm);
        assertEquals(farm.getName(), savedFarm.getName());
        verify(farmRepository, times(1)).save(farm);
    }

    @Test
    void saveFarm_InvalidName() {
        farm.setName("");  // Invalid name

        InvalidFarmException exception = assertThrows(InvalidFarmException.class, () -> farmService.save(farm));
        assertEquals("Farm name is required.", exception.getMessage());
    }

    @Test
    void findFarmById_Found() {
        when(farmRepository.findById(farm.getId())).thenReturn(Optional.of(farm));

        Optional<Farm> foundFarm = farmService.findById(farm.getId());

        assertTrue(foundFarm.isPresent());
        assertEquals(farm.getId(), foundFarm.get().getId());
    }

    @Test
    void findFarmById_NotFound() {
        when(farmRepository.findById(farm.getId())).thenReturn(Optional.empty());

        FarmNotFoundException exception = assertThrows(FarmNotFoundException.class, () -> farmService.findById(farm.getId()));
        assertEquals("Farm with ID " + farm.getId() + " not found.", exception.getMessage());  // Adjusted to match the actual message
    }

    @Test
    void getAllFarms() {
        Page<Farm> farms = new PageImpl<>(Arrays.asList(farm));
        when(farmRepository.findAll(any(Pageable.class))).thenReturn(farms);

        Page<Farm> result = farmService.findAll(mock(Pageable.class));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(farm.getName(), result.getContent().get(0).getName());
    }

    @Test
    void deleteFarm_WithFields() {
        farm.setFields(Arrays.asList(field));
        field.setId(UUID.randomUUID());

        when(farmRepository.findById(farm.getId())).thenReturn(Optional.of(farm));
        doNothing().when(fieldService).delete(any(UUID.class));
        doNothing().when(farmRepository).delete(farm);

        farmService.delete(farm);

        verify(fieldService, times(1)).delete(any(UUID.class));
        verify(farmRepository, times(1)).delete(farm);
    }

    @Test
    void deleteFarm_WithoutFields() {
        farm.setFields(null);

        when(farmRepository.findById(farm.getId())).thenReturn(Optional.of(farm));
        doNothing().when(farmRepository).delete(farm);

        farmService.delete(farm);

        verify(farmRepository, times(1)).delete(farm);
    }

    @Test
    void searchFarms_Valid() {
        when(farmRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(farm));

        List<Farm> result = farmService.searchFarms("Farm", "Location", LocalDate.now().minusDays(1));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(farm.getName(), result.get(0).getName());
    }

    @Test
    void searchFarms_InvalidName() {
        InvalidFarmException exception = assertThrows(InvalidFarmException.class, () -> farmService.searchFarms("", "Location", LocalDate.now()));
        assertEquals("Farm name cannot be empty.", exception.getMessage());
    }



    @Test
    void checkFieldArea_InvalidFieldSize() {
        field.setArea(0);

        InvalidFarmException exception = assertThrows(InvalidFarmException.class, () -> farmService.save(farm));
        assertEquals("Field area must be more than 0.", exception.getMessage());
    }
}
