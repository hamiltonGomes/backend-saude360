package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ProfessionalDto;
import com.saude360.backendsaude360.entities.Clinic;
import com.saude360.backendsaude360.entities.HealthSector;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.ClinicRepository;
import com.saude360.backendsaude360.repositories.HealthSectorRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final HealthSectorRepository healthSectorRepository;
    private final ClinicRepository clinicRepository;
    private static final String NOT_FOUND_MESSAGE = "Professional with ID: %d was not found.";

    @Autowired
    public ProfessionalService(ProfessionalRepository professionalRepository, ClinicRepository clinicRepository, HealthSectorRepository healthSectorRepository) {
        this.professionalRepository = professionalRepository;
        this.healthSectorRepository = healthSectorRepository;
        this.clinicRepository = clinicRepository;
    }

    public Professional create(ProfessionalDto professionalDto) {
        Professional professional = new Professional(professionalDto);
        addHealthSectorsToProfessional(professional, professionalDto.healthSectorsNames());
        addClinicsToProfessional(professional, professionalDto.clinicsId());
        return professionalRepository.save(professional);
    }

    private void addHealthSectorsToProfessional(Professional professional, @NotEmpty List<String> healthSectorNames) {
        for (String healthSectorName : healthSectorNames) {
            HealthSector healthSector = healthSectorRepository.findByName(healthSectorName)
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Health Sector with name: %s was not found.", healthSectorName)));
            if (!professional.getHealthSectors().contains(healthSector)) {
                professional.getHealthSectors().add(healthSector);
                healthSector.getProfessionals().add(professional);
            } else {
                throw new IllegalArgumentException(String.format("Health Sector with name: %s is already added to the professional.", healthSectorName));
            }
        }
    }

    private void addClinicsToProfessional(Professional professional, List<Long> clinicIds) {
        if (clinicIds != null) {
            for (Long clinicId : clinicIds) {
                if (clinicId != null && clinicRepository.existsById(clinicId)) {
                    Clinic clinic = clinicRepository.findById(clinicId)
                            .orElseThrow(() -> new ObjectNotFoundException(String.format("Clinic with ID: %d was not found.", clinicId)));
                    if (!professional.getClinics().contains(clinic)) {
                        professional.getClinics().add(clinic);
                        clinic.getProfessionals().add(professional);
                    } else {
                        throw new IllegalArgumentException(String.format("Clinic with ID: %d is already added to the professional.", clinicId));
                    }
                }
            }
        }
    }

    public Optional<Professional> update(Long id, ProfessionalDto professionalDto) {
        Optional<Professional> optionalProfessional = professionalRepository.findById(id);

        if (optionalProfessional.isPresent()) {
            Professional professional = optionalProfessional.get();
            BeanUtils.copyProperties(professionalDto, professional);
            return Optional.of(professionalRepository.save(professional));
        } else {
            throw new ObjectNotFoundException(String.format(String.format(NOT_FOUND_MESSAGE, id)));
        }
    }

    public Professional findById(Long id) {
        Optional<Professional> professional = professionalRepository.findById(id);
        return professional.orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
    }

    public List<Professional> findAll() {
        return professionalRepository.findAll();
    }

    public void delete(Long id) {
        try {
            professionalRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format(String.format(NOT_FOUND_MESSAGE, id)));
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
