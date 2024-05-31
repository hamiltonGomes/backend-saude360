package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.ProfessionalDto;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.entities.users.User;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.services.AddressService;
import com.saude360.backendsaude360.services.ProfessionalService;
import com.saude360.backendsaude360.services.UserService;
import com.saude360.backendsaude360.utils.BCryptPassword;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "user/professional")
public class ProfessionalController {
    private final ProfessionalService professionalService;
    private final UserService userService;
    private final AddressService addressService;

    public ProfessionalController(ProfessionalService professionalService, UserService userService, AddressService addressService) {
        this.professionalService = professionalService;
        this.userService = userService;
        this.addressService = addressService;
    }

    @PostMapping(value = "/")
    @Transactional
    public ResponseEntity<User> createProfessional(@RequestBody @Valid ProfessionalDto professionalDto) {
        try {
            Professional professional = new Professional(professionalDto);
            professional.setPassword(BCryptPassword.encryptPassword(professional));
            var newProfessional = userService.create(professional);

            var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newProfessional.getId()).toUri();
            return ResponseEntity.created(uri).body(newProfessional);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Unique field(s) already exist in the database");
        }
    }

    @PutMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Optional<Professional>> updateProfessional(@PathVariable Long id, @RequestBody @Valid ProfessionalDto professionalDto) {
        Optional<Professional> professionalUpdated = professionalService.update(id, professionalDto);
        if (professionalUpdated.isPresent() && professionalDto.address() != null) {
            addressService.update(professionalUpdated.get().getAddress().getId(), professionalDto.address());
        }
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
