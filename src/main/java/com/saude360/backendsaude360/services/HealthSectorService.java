package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.HealthSectorDto;
import com.saude360.backendsaude360.entities.HealthSector;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.HealthSectorRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HealthSectorService {
    private final HealthSectorRepository healthSectorRepository;
    private final ProfessionalRepository professionalRepository;
    private static final String NOT_FOUND_MESSAGE = "Health Sector with ID: %d was not found.";

    @Autowired
    public HealthSectorService(HealthSectorRepository healthSectorRepository, ProfessionalRepository professionalRepository) {
        this.healthSectorRepository = healthSectorRepository;
        this.professionalRepository = professionalRepository;
    }

    public HealthSector create(HealthSectorDto healthSectorDto) {
        if (healthSectorRepository.existsByName(healthSectorDto.name())) {
            throw new DatabaseException("Health Sector with name: " + healthSectorDto.name() + " already exists.");
        }
        HealthSector healthSector = new HealthSector(healthSectorDto);
        if (healthSectorDto.professionalId() != null) {
            Professional professional = professionalRepository.findById(healthSectorDto.professionalId())
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Professional with ID: %d was not found.", healthSectorDto.professionalId())));
            healthSector.addProfessional(professional);
        }
        return healthSectorRepository.save(healthSector);
    }

    public HealthSector findById(Long id) {
        Optional<HealthSector> healthSector = healthSectorRepository.findById(id);
        return healthSector.orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
    }

    public List<HealthSector> findAll() {
        return healthSectorRepository.findAll();
    }

    public void delete(Long healthSectorId) {
        HealthSector healthSector = healthSectorRepository.findById(healthSectorId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, healthSectorId)));

        for (Professional professional : new ArrayList<>(healthSector.getProfessionals())) {
            healthSector.removeProfessional(professional);
            professionalRepository.save(professional);
        }

        healthSectorRepository.delete(healthSector);
    }

    public Optional<HealthSector> update(Long id, HealthSectorDto healthSectorDto) {
        HealthSector healthSector = healthSectorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
        if (!healthSector.getName().equals(healthSectorDto.name()) && healthSectorRepository.existsByName(healthSectorDto.name())) {
            throw new DatabaseException("Health Sector with name: " + healthSectorDto.name() + " already exists.");
        }
        healthSector.setName(healthSectorDto.name());
        return Optional.of(healthSectorRepository.save(healthSector));
    }

    public HealthSector findByName(String name) {
        return healthSectorRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Health Sector with name: %s was not found.", name)));
    }
}