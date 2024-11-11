package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.HealthSectorDto;
import com.saude360.backendsaude360.entities.HealthSector;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.HealthSectorRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthSectorServiceTest {

    @InjectMocks
    private HealthSectorService healthSectorService;

    @Mock
    private HealthSectorRepository healthSectorRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateHealthSectorSuccess() {
        HealthSectorDto dto = new HealthSectorDto("Cardiologia", 1L);
        HealthSector healthSector = new HealthSector(dto);
        Professional professional = new Professional();
        professional.setId(1L);

        when(healthSectorRepository.existsByName(dto.name())).thenReturn(false);
        when(professionalRepository.findById(dto.professionalId())).thenReturn(Optional.of(professional));
        when(healthSectorRepository.save(any(HealthSector.class))).thenReturn(healthSector);

        HealthSector result = healthSectorService.create(dto);

        assertNotNull(result);
        assertEquals(dto.name(), result.getName());
        verify(healthSectorRepository, times(1)).save(any(HealthSector.class));
    }

    @Test
    void testCreateHealthSectorDuplicateNameThrowsException() {
        HealthSectorDto dto = new HealthSectorDto("Cardiologia", null);

        when(healthSectorRepository.existsByName(dto.name())).thenReturn(true);

        assertThrows(DatabaseException.class, () -> healthSectorService.create(dto));
        verify(healthSectorRepository, never()).save(any(HealthSector.class));
    }

    @Test
    void testCreateHealthSectorWithNonExistentProfessionalThrowsException() {
        HealthSectorDto dto = new HealthSectorDto("Cardiologia", 1L);

        when(healthSectorRepository.existsByName(dto.name())).thenReturn(false);
        when(professionalRepository.findById(dto.professionalId())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> healthSectorService.create(dto));
        verify(healthSectorRepository, never()).save(any(HealthSector.class));
    }

    @Test
    void testFindByIdSuccess() {
        Long id = 1L;
        HealthSector healthSector = new HealthSector();
        healthSector.setId(id);

        when(healthSectorRepository.findById(id)).thenReturn(Optional.of(healthSector));

        HealthSector result = healthSectorService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindByIdNotFoundThrowsException() {
        Long id = 1L;

        when(healthSectorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> healthSectorService.findById(id));
    }

    @Test
    void testFindAll() {
        List<HealthSector> sectors = new ArrayList<>();
        sectors.add(new HealthSector());

        when(healthSectorRepository.findAll()).thenReturn(sectors);

        List<HealthSector> result = healthSectorService.findAll();

        assertEquals(1, result.size());
        verify(healthSectorRepository, times(1)).findAll();
    }

    @Test
    void testDeleteHealthSectorSuccess() {
        Long healthSectorId = 1L;
        HealthSector healthSector = new HealthSector();
        healthSector.setId(healthSectorId);
        Professional professional = new Professional();
        healthSector.addProfessional(professional);

        when(healthSectorRepository.findById(healthSectorId)).thenReturn(Optional.of(healthSector));

        healthSectorService.delete(healthSectorId);

        verify(healthSectorRepository, times(1)).delete(healthSector);
        verify(professionalRepository, times(1)).save(professional);
    }

    @Test
    void testDeleteHealthSectorNotFoundThrowsException() {
        Long healthSectorId = 1L;

        when(healthSectorRepository.findById(healthSectorId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> healthSectorService.delete(healthSectorId));
    }

    @Test
    void testUpdateHealthSectorSuccess() {
        Long id = 1L;
        HealthSectorDto dto = new HealthSectorDto("Neurologia", null);
        HealthSector healthSector = new HealthSector();
        healthSector.setId(id);
        healthSector.setName("Cardiologia");

        when(healthSectorRepository.findById(id)).thenReturn(Optional.of(healthSector));
        when(healthSectorRepository.existsByName(dto.name())).thenReturn(false);
        when(healthSectorRepository.save(any(HealthSector.class))).thenReturn(healthSector);

        Optional<HealthSector> result = healthSectorService.update(id, dto);

        assertTrue(result.isPresent());
        assertEquals(dto.name(), result.get().getName());
        verify(healthSectorRepository, times(1)).save(healthSector);
    }

    @Test
    void testUpdateHealthSectorDuplicateNameThrowsException() {
        Long id = 1L;
        HealthSectorDto dto = new HealthSectorDto("Neurologia", null);
        HealthSector healthSector = new HealthSector();
        healthSector.setId(id);
        healthSector.setName("Cardiologia");

        when(healthSectorRepository.findById(id)).thenReturn(Optional.of(healthSector));
        when(healthSectorRepository.existsByName(dto.name())).thenReturn(true);

        assertThrows(DatabaseException.class, () -> healthSectorService.update(id, dto));
        verify(healthSectorRepository, never()).save(any(HealthSector.class));
    }

    @Test
    void testFindByNameSuccess() {
        String name = "Cardiologia";
        HealthSector healthSector = new HealthSector();
        healthSector.setName(name);

        when(healthSectorRepository.findByName(name)).thenReturn(Optional.of(healthSector));

        HealthSector result = healthSectorService.findByName(name);

        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    void testFindByNameNotFoundThrowsException() {
        String name = "Cardiologia";

        when(healthSectorRepository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> healthSectorService.findByName(name));
    }
}
