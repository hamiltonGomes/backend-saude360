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
    private static final String NOT_FOUND_MESSAGE = "Setor de Saúde com ID: %d não foi encontrado.";

    @Autowired
    public HealthSectorService(HealthSectorRepository healthSectorRepository, ProfessionalRepository professionalRepository) {
        this.healthSectorRepository = healthSectorRepository;
        this.professionalRepository = professionalRepository;
    }

    public HealthSector create(HealthSectorDto healthSectorDto) {
        if (healthSectorRepository.existsByName(healthSectorDto.name())) {
            throw new DatabaseException("Setor de Saúde com nome: " + healthSectorDto.name() + " já existe.");
        }
        HealthSector healthSector = new HealthSector(healthSectorDto);
        if (healthSectorDto.professionalId() != null) {
            Professional professional = professionalRepository.findById(healthSectorDto.professionalId())
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Profissional com ID: %d não foi encontrado.", healthSectorDto.professionalId())));
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
            throw new DatabaseException("Setor de Saúde com nome: " + healthSectorDto.name() + " já existe.");
        }
        healthSector.setName(healthSectorDto.name());
        return Optional.of(healthSectorRepository.save(healthSector));
    }

    public HealthSector findByName(String name) {
        return healthSectorRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Setor de Saúde com nome: %s não foi encontrado.", name)));
    }
}