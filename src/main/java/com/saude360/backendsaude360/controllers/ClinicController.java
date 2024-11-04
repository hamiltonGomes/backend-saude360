package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.ClinicDto;
import com.saude360.backendsaude360.dtos.ClinicUpdateDto;
import com.saude360.backendsaude360.entities.Clinic;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.services.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "clinic")
public class ClinicController {
    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping(value = "/")
    @Transactional
    public ResponseEntity<Clinic> createClinic(@RequestBody @Valid ClinicDto clinicDto) {
        try {
            Clinic newClinic = clinicService.create(clinicDto);
            var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newClinic.getId()).toUri();
            return ResponseEntity.created(uri).body(newClinic);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Unique field(s) already exist in the database");
        }
    }

    @PutMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Optional<Clinic>> updateClinic(@PathVariable Long id, @RequestBody @Valid ClinicUpdateDto clinicUpdateDto) {
        Optional<Clinic> clinicUpdated = clinicService.update(id, clinicUpdateDto);
        return ResponseEntity.ok().body(clinicUpdated);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Clinic> findClinicById(@PathVariable Long id) {
        var clinic = clinicService.findById(id);
        return ResponseEntity.ok().body(clinic);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Clinic>> findAllClinics() {
        var clinics = clinicService.findAll();
        return ResponseEntity.ok().body(clinics);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Clinic> deleteClinicById(@PathVariable Long id) {
        clinicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
