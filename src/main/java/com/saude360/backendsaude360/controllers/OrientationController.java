package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.OrientationDto;
import com.saude360.backendsaude360.dtos.OrientationWithResponsesDto;
import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.services.OrientationService;
import com.saude360.backendsaude360.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orientation")
public class OrientationController {

    private final OrientationService orientationService;
    private final PatientService patientService;

    @Autowired
    public OrientationController(OrientationService orientationService, PatientService patientService) {
        this.orientationService = orientationService;
        this.patientService = patientService;
    }

    @PostMapping("patient/{patientId}")
    public ResponseEntity<Orientation> createOrientation(@PathVariable Long patientId, @RequestBody OrientationDto orientationDto) {
        Patient patient = patientService.findById(patientId);
        Orientation orientation = orientationService.create(orientationDto, patient);
        return ResponseEntity.ok(orientation);
    }

    @GetMapping("patient/{patientId}")
    public ResponseEntity<List<OrientationWithResponsesDto>> findAllOrientationsByPatientId(@PathVariable Long patientId) {
//        List<Orientation> orientationsWithResponses = orientationService.findAllByPatientId(patientId);
        List<OrientationWithResponsesDto> orientationsWithResponses = orientationService.findAllWithResponsesByPatientId(patientId);
        return ResponseEntity.ok(orientationsWithResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orientation> findOrientationById(@PathVariable Long id) {
        Orientation orientation = orientationService.findById(id);
        return ResponseEntity.ok(orientation);
    }

    @GetMapping("/")
    public ResponseEntity<List<Orientation>> findAllOrientations() {
        var orientation = orientationService.findAll();
        return ResponseEntity.ok(orientation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orientationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orientation> update(@PathVariable Long id, @RequestBody OrientationDto orientationDto) {
        Optional<Orientation> optionalOrientation = orientationService.update(id, orientationDto);
        return optionalOrientation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}