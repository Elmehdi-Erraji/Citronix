/*
package com.spring.citronix;

import com.spring.citronix.domain.Farm;
import com.spring.citronix.domain.Field;
import com.spring.citronix.repository.FieldRepository;
import com.spring.citronix.service.imp.FieldServiceImp;
import com.spring.citronix.web.errors.FieldNotFoundException;
import com.spring.citronix.web.errors.InvalidFieldAreaException;
import com.spring.citronix.web.errors.MaxFieldsInFarmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FieldServiceTest {

    @Mock
    private FieldRepository fieldRepository;

    @InjectMocks
    private FieldServiceImp fieldService;

    private Farm farm;
    private Field field;*/
/**//*


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        farm = new Farm();
        farm.setId(UUID.randomUUID());
        farm.setArea(1000);
        farm.setFields(new ArrayList<>());


        field = new Field();
        field.setId(UUID.randomUUID());
        field.setArea(200);
        field.setFarm(farm);
    }

    @Test
    void testSave_validField() {
        when(fieldRepository.save(any(Field.class))).thenReturn(field);
        when(fieldRepository.findFieldByFarmId(any(UUID.class))).thenReturn(new ArrayList<>());

        Field savedField = fieldService.save(field);

        assertNotNull(savedField);
        verify(fieldRepository, times(1)).save(field);
    }

    @Test
    void testSave_invalidFieldArea() {
        field.setArea(600);
        when(fieldRepository.findFieldByFarmId(any(UUID.class))).thenReturn(new ArrayList<>());

        assertThrows(InvalidFieldAreaException.class, () -> fieldService.save(field));
    }

    @Test
    void testDelete_fieldNotFound() {
        UUID fieldId = UUID.randomUUID();
        when(fieldRepository.existsById(fieldId)).thenReturn(false);

        assertThrows(FieldNotFoundException.class, () -> fieldService.delete(fieldId));
    }

    @Test
    void testDelete_validDelete() {
        UUID fieldId = field.getId();
        when(fieldRepository.existsById(fieldId)).thenReturn(true);

        fieldService.delete(fieldId);

        verify(fieldRepository, times(1)).deleteById(fieldId);
    }

    @Test
    void testFindByFarmId() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        when(fieldRepository.findByFarmId(farm.getId(), pageable)).thenReturn(Page.empty());

        Page<Field> result = fieldService.findByFarmId(farm.getId(), pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void testValidateFieldArea_maxFieldsInFarm() {
        List<Field> fields = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            fields.add(new Field());
        }
        farm.setFields(fields);
        field.setFarm(farm);
        assertThrows(MaxFieldsInFarmException.class, () -> fieldService.save(field));
    }

    @Test
    void testValidateFieldArea_totalAreaExceeded() {
        field.setArea(600);
        when(fieldRepository.findFieldByFarmId(any(UUID.class))).thenReturn(List.of(new Field() {{
            setArea(500);
        }}));

        assertThrows(InvalidFieldAreaException.class, () -> fieldService.save(field));
    }
}
*/
