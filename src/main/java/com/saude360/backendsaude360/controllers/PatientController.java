package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.AddressDto;
import com.saude360.backendsaude360.dtos.PatientDto;
import com.saude360.backendsaude360.entities.Address;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.services.PatientService;
import com.saude360.backendsaude360.services.ProfessionalService;
import com.saude360.backendsaude360.services.UserService;
import com.saude360.backendsaude360.utils.BCryptPassword;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "user/patient")
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;
    private final ProfessionalService professionalService;

    @Autowired
    public PatientController(PatientService patientService, UserService userService, ProfessionalService professionalService) {
        this.patientService = patientService;
        this.professionalService = professionalService;
        this.userService = userService;
    }

    @PostMapping(value = "professional/{professionalId}")
    @Transactional
    public ResponseEntity<User> createPatient(@PathVariable Long professionalId, @RequestBody @Valid PatientDto patientDto) {
        try {
            Professional professional = professionalService.findById(professionalId);
            var address = new Address(patientDto.address());
            Patient patient = new Patient(patientDto, professional, address);
            patient.setPassword(BCryptPassword.encryptPassword(patient));

            User newPatient = userService.create(patient);
            var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{patientId}")
                    .buildAndExpand(newPatient.getId()).toUri();
            return ResponseEntity.created(uri).body(newPatient);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Unique field(s) already exist in the database");
        }
    }

    @PutMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Optional<Patient>> updatePatient(@PathVariable Long id, @RequestBody @Valid PatientDto patientDto) {
        Optional<Patient> patientUpdated = patientService.update(id, patientDto);
        return ResponseEntity.ok().body(patientUpdated);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Patient> findPatientById(@PathVariable Long id) {
        var patient = patientService.findById(id);
        return ResponseEntity.ok().body(patient);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Patient>> findAllPatients() {
        var patients = patientService.findAll();
        return ResponseEntity.ok().body(patients);
    }

}
