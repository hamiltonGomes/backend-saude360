package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.PatientDto;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.services.AddressService;
import com.saude360.backendsaude360.services.PatientService;
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

    public PatientController(PatientService patientService, UserService userService, AddressService addressService) {
        this.patientService = patientService;
        this.userService = userService;
        this.addressService = addressService;
    }

    @PostMapping(value = "/create")
    @Transactional
    public ResponseEntity<User> createPatient(@RequestBody @Valid PatientDto patientDto) {
        try {
            Patient patient = new Patient(patientDto);
            patient.setPassword(BCryptPassword.encryptPassword(patient));
            var newPatient = userService.create(patient);

            var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
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
        if (patientUpdated.isPresent() && patientDto.address() != null) {
            addressService.update(patientUpdated.get().getAddress().getId(), patientDto.address());
        }
        return ResponseEntity.ok().body(patientUpdated);
    }
}
