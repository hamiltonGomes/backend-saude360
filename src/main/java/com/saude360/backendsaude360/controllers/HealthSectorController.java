package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.HealthSectorDto;
import com.saude360.backendsaude360.entities.HealthSector;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.services.HealthSectorService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "healthSector")
public class HealthSectorController {

    private final HealthSectorService healthSectorService;

    public HealthSectorController(HealthSectorService healthSectorService) {
        this.healthSectorService = healthSectorService;
    }

    @PostMapping(value = "/")
    @Transactional
    public ResponseEntity<HealthSector> createHealthSector(@RequestBody @Valid HealthSectorDto healthSectorDto) {
        try {
            HealthSector healthSector = healthSectorService.create(healthSectorDto);
            var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(healthSector.getId()).toUri();
            return ResponseEntity.created(uri).body(healthSector);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Unique field(s) already exist in the database");
        }
    }

    @PutMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Optional<HealthSector>> updateHealthSector(@PathVariable Long id, @RequestBody @Valid HealthSectorDto healthSectorDto) {
        Optional<HealthSector> healthSectorUpdated = healthSectorService.update(id, healthSectorDto);
        return ResponseEntity.ok().body(healthSectorUpdated);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HealthSector> findHealthSectorById(@PathVariable Long id) {
        var healthSector = healthSectorService.findById(id);
        return ResponseEntity.ok().body(healthSector);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<HealthSector>> findAllHealthSectors() {
        var healthSectors = healthSectorService.findAll();
        return ResponseEntity.ok().body(healthSectors);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HealthSector> deleteHealthSectorById(@PathVariable Long id) {
        healthSectorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}