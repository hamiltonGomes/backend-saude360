package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.AddressUpdateDto;
import com.saude360.backendsaude360.dtos.ClinicUpdateDto;
import com.saude360.backendsaude360.dtos.ProfessionalUpdateDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import com.saude360.backendsaude360.entities.Clinic;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.ClinicRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import com.saude360.backendsaude360.utils.BCryptPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfessionalServiceTest {

    @InjectMocks
    private ProfessionalService professionalService;

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private ClinicRepository clinicRepository;

    private Professional professional;
    private ProfessionalUpdateDto professionalUpdateDto;
    private ClinicUpdateDto clinicUpdateDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        professional = new Professional();
        professional.setCpf("12345678901");
        professional.setPassword(BCryptPassword.encryptPassword("Test1234"));

        AddressUpdateDto addressUpdateDto = new AddressUpdateDto("12345-678", "SP", "São Paulo", "Centro", "Rua A", 123, "Apt 1");

        clinicUpdateDto = new ClinicUpdateDto("12345678000195", "(11)91234-5678", "(11)1234-5678", "1234567", addressUpdateDto);

        professionalUpdateDto = new ProfessionalUpdateDto(
                "Novo Nome",
                "(11)91234-5678",
                "NovaSenha123",
                null,
                "123456789012345",
                List.of("Cardiologia"),
                List.of(clinicUpdateDto)
        );
    }

    @Test
    void testCreateProfessional() {
        when(professionalRepository.save(any(Professional.class))).thenReturn(professional);
        Professional savedProfessional = professionalService.create(professional);

        assertNotNull(savedProfessional);
        verify(professionalRepository, times(1)).save(professional);
    }

    @Test
    void testUpdateProfessional_WithValidData() {
        UserDetails userDetails = User.withUsername("12345678901").password("Test1234").authorities("PROFESSIONAL").build();
        SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        professional = new Professional();
        professional.setCpf("12345678901");
        professional.setFullName("Nome Original");

        clinicUpdateDto = new ClinicUpdateDto("12345678901234", "999999999", "888888888", "1234567", new AddressUpdateDto("12345-678", "Estado", "Cidade", "Bairro", "Rua", 123, "Complemento"));

        when(professionalRepository.findByCpf("12345678901")).thenReturn(professional);
        when(professionalRepository.save(any(Professional.class))).thenReturn(professional);
        when(clinicRepository.findByCnpj(clinicUpdateDto.cnpj())).thenReturn(Optional.of(new Clinic()));

        Optional<Professional> updatedProfessional = professionalService.update(professionalUpdateDto);

        assertTrue(updatedProfessional.isPresent());
        assertEquals("Novo Nome", updatedProfessional.get().getFullName());
        verify(professionalRepository, times(1)).save(any(Professional.class));
    }

    @Test
    void testFindById_WithExistingId() {
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));

        Professional foundProfessional = professionalService.findById(1L);

        assertNotNull(foundProfessional);
        verify(professionalRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_WithNonExistingId() {
        when(professionalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> professionalService.findById(1L));
    }

    @Test
    void testDelete_WithExistingId() {
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));

        professionalService.delete(1L);

        verify(professionalRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_ThrowsObjectNotFoundException() {
        doThrow(new EmptyResultDataAccessException(1)).when(professionalRepository).deleteById(1L);

        assertThrows(ObjectNotFoundException.class, () -> professionalService.delete(1L));
    }

    @Test
    void testDelete_ThrowsDatabaseException() {
        doThrow(new DataIntegrityViolationException("Integrity violation")).when(professionalRepository).deleteById(1L);

        assertThrows(DatabaseException.class, () -> professionalService.delete(1L));
    }
}
