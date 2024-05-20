package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.PatientDto;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.services.AddressService;
import com.saude360.backendsaude360.services.PatientService;
import com.saude360.backendsaude360.services.ProfessionalService;
import com.saude360.backendsaude360.services.UserService;
import com.saude360.backendsaude360.utils.BCryptPassword;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping(value = "user/patient")
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;
    private final AddressService addressService;
    private final ProfessionalService professionalService;

    public PatientController(PatientService patientService, UserService userService, AddressService addressService, ProfessionalService professionalService) {
        this.patientService = patientService;
        this.professionalService = professionalService;
        this.addressService = addressService;
        this.userService = userService;
    }

    @PostMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<User> createPatient(@PathVariable Long id, @RequestBody @Valid PatientDto patientDto) {
        try {
            Professional professional = professionalService.findById(id);
            Patient patient = new Patient(patientDto, professional);
            patient.setPassword(BCryptPassword.encryptPassword(patient));

            User newPatient = userService.create(patient);

            var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newPatient.getId()).toUri();
            return ResponseEntity.created(uri).body(newPatient);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Unique field(s) already exist in the database");
        }
    }

    @PutMapping(value = "/{professionalId}/{patientId}")
    @Transactional
    public ResponseEntity<Optional<Patient>> updatePatient(@PathVariable Long professionalId, @PathVariable Long patientId, @RequestBody @Valid PatientDto patientDto) {
        Optional<Patient> patientUpdated = patientService.update(patientId, patientDto);
        if (patientUpdated.isPresent() && patientDto.address() != null) {
            addressService.update(patientUpdated.get().getAddress().getId(), patientDto.address());
        }
        return ResponseEntity.ok().body(patientUpdated);
    }
}
