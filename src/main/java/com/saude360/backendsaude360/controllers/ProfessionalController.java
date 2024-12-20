package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.ClinicDto;
import com.saude360.backendsaude360.dtos.ProfessionalDto;
import com.saude360.backendsaude360.dtos.ProfessionalUpdateDto;
import com.saude360.backendsaude360.entities.Clinic;
import com.saude360.backendsaude360.entities.HealthSector;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.entities.users.UserRole;
import com.saude360.backendsaude360.enums.UserRoles;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.services.HealthSectorService;
import com.saude360.backendsaude360.services.ProfessionalService;
import com.saude360.backendsaude360.services.UserService;
import com.saude360.backendsaude360.utils.BCryptPassword;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "user/professional")
public class ProfessionalController {
    private final ProfessionalService professionalService;
    private final UserService userService;
    private final HealthSectorService healthSectorService;

    public ProfessionalController(ProfessionalService professionalService, UserService userService, HealthSectorService healthSectorService) {
        this.professionalService = professionalService;
        this.userService = userService;
        this.healthSectorService = healthSectorService;
    }

    @PostMapping(value = "/")
    @Transactional
    public ResponseEntity<User> createProfessional(@RequestBody @Valid ProfessionalDto professionalDto) {
        try {
            List<HealthSector> healthSectors = new ArrayList<>();
            for(String h : professionalDto.healthSectorsNames()) {
                var obj = healthSectorService.findByName(h);
                healthSectors.add(obj);
            }
            var professional = new Professional(professionalDto, healthSectors);

            if(professionalDto.clinic() != null && !professionalDto.clinic().isEmpty()) {
                List<Clinic> clinics = new ArrayList<>();
                for(ClinicDto clinic: professionalDto.clinic()) {
                    var obj = new Clinic(clinic);
                    clinics.add(obj);
                }
                professional = new Professional(professionalDto, healthSectors, clinics);
            }

            professional.setPassword(BCryptPassword.encryptPassword(professional));
            UserRole professionalRole = new UserRole(null, UserRoles.ROLE_PROFESSIONAL);

            professional.addRole(professionalRole);

            var newProfessional = userService.create(professional);

            var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newProfessional.getId()).toUri();
            return ResponseEntity.created(uri).body(newProfessional);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Unique field(s) already exist in the database");
        }
    }

    @PutMapping(value = "/")
    @Transactional
    public ResponseEntity<Optional<Professional>> updateProfessional(@RequestBody @Valid ProfessionalUpdateDto professionalUpdateDto) {
        Optional<Professional> professionalUpdated = professionalService.update(professionalUpdateDto);
        return ResponseEntity.ok().body(professionalUpdated);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Professional> findProfessionalById(@PathVariable Long id) {
        var professional = professionalService.findById(id);
        return ResponseEntity.ok().body(professional);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Professional>> findAllProfessionals() {
        var professionals = professionalService.findAll();
        return ResponseEntity.ok().body(professionals);
    }

}
