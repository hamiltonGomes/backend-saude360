package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.users.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setId(1L);
        patient.setCpf("12345678900");
        patient.setFullName("John Doe");
        patient.setPassword("oldPassword");

        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.of(patient));
    }

    @Test
    void testFindPatientById_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient foundPatient = patientService.findById(1L);

        assertNotNull(foundPatient);
        assertEquals(1L, foundPatient.getId());
        assertEquals("John Doe", foundPatient.getFullName());
    }

    @Test
    void testFindPatientById_NotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> patientService.findById(1L));
    }

    @Test
    void testDeletePatient_Success() {
        doNothing().when(patientRepository).deleteById(1L);

        assertDoesNotThrow(() -> patientService.delete(1L));
        verify(patientRepository).deleteById(1L);
    }

    @Test
    void testDeletePatient_NotFound() {
        doThrow(new EmptyResultDataAccessException(1)).when(patientRepository).deleteById(1L);

        assertThrows(ObjectNotFoundException.class, () -> patientService.delete(1L));
    }
}
