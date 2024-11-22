package com.spring.citronix;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;
import com.spring.citronix.repository.FarmRepository;
import com.spring.citronix.service.FieldService;
import com.spring.citronix.service.imp.FarmServiceImp;
import com.spring.citronix.web.errors.FarmNotFoundException;
import com.spring.citronix.web.errors.InvalidFarmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FarmServiceTest {

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FieldService fieldService;

    @InjectMocks
    private FarmServiceImp farmService;


    private Farm createValidFarm() {
        return new Farm(
                UUID.randomUUID(),
                "Farm A",
                "Location A",
                100.0,
                LocalDate.now(),
                List.of()
        );
    }


    private Farm createInvalidFarm() {
        return new Farm(
                UUID.randomUUID(),
                "",
                "",
                -10.0,
                LocalDate.now().plusDays(1),
                List.of()
        );
    }

    @Test
    void save_ValidFarm_Success() {
        Farm farm = createValidFarm();
        when(farmRepository.save(any(Farm.class))).thenReturn(farm);

        Farm savedFarm = farmService.save(farm);

        assertNotNull(savedFarm);
        assertEquals("Farm A", savedFarm.getName());
        verify(farmRepository, times(1)).save(farm);
    }

    @Test
    void save_InvalidFarm_ThrowsException() {
        Farm invalidFarm = createInvalidFarm();
        assertThrows(InvalidFarmException.class, () -> farmService.save(invalidFarm));
        verify(farmRepository, never()).save(any());
    }

    @Test
    void findById_ValidId_Success() {
        UUID farmId = UUID.randomUUID();
        Farm farm = createValidFarm();
        when(farmRepository.findById(farmId)).thenReturn(Optional.of(farm));

        Optional<Farm> foundFarm = farmService.findById(farmId);

        assertTrue(foundFarm.isPresent());
        assertEquals("Farm A", foundFarm.get().getName());
        verify(farmRepository, times(1)).findById(farmId);
    }

    @Test
    void findById_InvalidId_ThrowsException() {
        UUID farmId = UUID.randomUUID();
        when(farmRepository.findById(farmId)).thenReturn(Optional.empty());

        assertThrows(FarmNotFoundException.class, () -> farmService.findById(farmId));
        verify(farmRepository, times(1)).findById(farmId);
    }

    @Test
    void findAll_WithPageable_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Farm farm = createValidFarm();
        Page<Farm> farmsPage = new PageImpl<>(List.of(farm));
        when(farmRepository.findAll(pageable)).thenReturn(farmsPage);

        Page<Farm> result = farmService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Farm A", result.getContent().get(0).getName());
        verify(farmRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAll_NoFarms_ReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(farmRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<Farm> result = farmService.findAll(pageable);

        assertTrue(result.isEmpty());
        verify(farmRepository, times(1)).findAll(pageable);
    }

}