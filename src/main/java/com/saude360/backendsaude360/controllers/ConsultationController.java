package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.ConsultationDto;
import com.saude360.backendsaude360.dtos.ConsultationUpdateDto;
import com.saude360.backendsaude360.entities.Consultation;
import com.saude360.backendsaude360.services.ConsultationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "consultation")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<Consultation> create(@RequestBody @Valid ConsultationDto consultationDto) {
        Consultation consultation = consultationService.create(consultationDto);
        return ResponseEntity.ok().body(consultation);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Consultation> update(@PathVariable Long id, @RequestBody @Valid ConsultationUpdateDto consultationUpdateDto) {
        Consultation consultation = consultationService.update(id, consultationUpdateDto);
        return ResponseEntity.ok().body(consultation);
    }

    @GetMapping("/")
    public ResponseEntity<List<Consultation>> findAll() {
        List<Consultation> consultations = consultationService.findAll();
        return ResponseEntity.ok().body(consultations);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Consultation>> findAllConsultationsByPatientAndProfessional(@PathVariable Long patientId) {
        List<Consultation> consultations = consultationService.findAllConsultationsByPatientAndProfessional(patientId);
        return ResponseEntity.ok().body(consultations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultation> findById(@PathVariable Long id) {
        Consultation consultation = consultationService.findById(id);
        return ResponseEntity.ok().body(consultation);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        consultationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
