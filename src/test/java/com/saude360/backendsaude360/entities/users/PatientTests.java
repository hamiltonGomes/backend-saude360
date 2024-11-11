package com.saude360.backendsaude360.entities.users;

import com.saude360.backendsaude360.dtos.AddressDto;
import com.saude360.backendsaude360.dtos.PatientDto;
import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.Consultation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PatientTests {

    private Patient patient;
    private Professional professional;

    @BeforeEach
    void setUp() {
        professional = new Professional();
        professional.setFullName("Dr. John Doe");

        PatientDto patientDto = new PatientDto(
                "Jane Doe",
                "12345678901",
                LocalDate.of(1990, 1, 1),
                "jane.doe@example.com",
                null,
                "(11)91234-5678",
                null,
                new AddressDto("12345-678", "State", "City", "Neighborhood", "Street", 123, "Apt 1B"),
                "No comments"
        );

        patient = new Patient(patientDto, professional);
    }

    @Test
    void testPatientInitialization() {
        assertNotNull(patient);
        assertEquals("Jane Doe", patient.getFullName());
        assertEquals("12345678901", patient.getCpf());
        assertEquals(LocalDate.of(1990, 1, 1), patient.getBirthDate());
        assertEquals("jane.doe@example.com", patient.getEmail());
        assertEquals("(11)91234-5678", patient.getPhoneNumber());
        assertEquals(1, patient.getProfessionals().size());
        assertTrue(patient.getProfessionals().contains(professional));
        assertNotNull(patient.getAddress());
        assertEquals("Street", patient.getAddress().getStreet());
        assertEquals("City", patient.getAddress().getCity());
        assertEquals("State", patient.getAddress().getState());
        assertEquals("12345-678", patient.getAddress().getCep());
        assertEquals(123, patient.getAddress().getNumber());
        assertEquals("Neighborhood", patient.getAddress().getNeighborhood());
        assertEquals("Apt 1B", patient.getAddress().getComplement());
        assertEquals(1, patient.getComments().size());
        assertEquals("No comments", patient.getComments().getFirst());
    }

    @Test
    void testGenerateRandomPassword() {
        String password1 = patient.generateRandomPassword();
        String password2 = patient.generateRandomPassword();

        assertNotNull(password1, "A primeira senha não deve ser nula.");
        assertNotNull(password2, "A segunda senha não deve ser nula.");
        assertFalse(password1.isEmpty(), "A primeira senha não deve estar vazia.");
        assertFalse(password2.isEmpty(), "A segunda senha não deve estar vazia.");

        assertNotEquals(password1, password2, "As senhas geradas devem ser diferentes.");
    }

    @Test
    void testAddProfessional() {
        Professional newProfessional = new Professional();
        newProfessional.setFullName("Dr. Hamilton Gomes");
        patient.getProfessionals().add(newProfessional);
        assertEquals(2, patient.getProfessionals().size());
    }

    @Test
    void testAddOrientation() {
        Orientation orientation = new Orientation();
        orientation.setPatient(patient);
        patient.getOrientations().add(orientation);
        assertEquals(1, patient.getOrientations().size());
    }

    @Test
    void testAddConsultation() {
        Consultation consultation = new Consultation();
        consultation.setPatient(patient);
        patient.getConsultations().add(consultation);
        assertEquals(1, patient.getConsultations().size());
    }
}
