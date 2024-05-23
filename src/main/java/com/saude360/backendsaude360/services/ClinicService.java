package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ClinicDto;
import com.saude360.backendsaude360.entities.Clinic;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.ClinicRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final ProfessionalRepository professionalRepository;
    private static final String NOT_FOUND_MESSAGE = "Clinic with ID: %d was not found.";

    @Autowired
    public ClinicService(ClinicRepository clinicRepository, ProfessionalRepository professionalRepository) {
        this.clinicRepository = clinicRepository;
        this.professionalRepository = professionalRepository;
    }

    public Clinic create(ClinicDto clinicDto) {
        Clinic clinic = new Clinic(clinicDto);
        addProfessionalsToClinic(clinic, clinicDto.professionalIds());
        return clinicRepository.save(clinic);
    }

    private void addProfessionalsToClinic(Clinic clinic, List<Long> professionalIds) {
        List<Professional> currentProfessionals = new ArrayList<>(clinic.getProfessionals());
        for (Professional professional : currentProfessionals) {
            clinic.removeProfessional(professional);
            professionalRepository.save(professional);
        }

        for (Long professionalId : professionalIds) {
            Professional professional = professionalRepository.findById(professionalId)
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Professional with ID: %d was not found.", professionalId)));

            clinic.addProfessional(professional);
            professionalRepository.save(professional);
        }

        clinicRepository.save(clinic);
    }

    public Clinic findById(Long id) {
        Optional<Clinic> clinic = clinicRepository.findById(id);
        return clinic.orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
    }

    public List<Clinic> findAll() {
        return clinicRepository.findAll();
    }

    public void delete(Long clinicId) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, clinicId)));

        for (Professional professional : new ArrayList<>(clinic.getProfessionals())) {
            clinic.removeProfessional(professional);
            professionalRepository.save(professional);
        }

        clinicRepository.deleteById(clinicId);
    }

    public Optional<Clinic> update(Long id, ClinicDto clinicDto, List<Long> professionalIds) {
        Optional<Clinic> optionalClinic = clinicRepository.findById(id);
        if (optionalClinic.isPresent()) {
            Clinic clinic = optionalClinic.get();
            addProfessionalsToClinic(clinic, professionalIds);

            BeanUtils.copyProperties(clinicDto, clinic, "id");
            return Optional.of(clinicRepository.save(clinic));
        } else {
            throw new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id));
        }
    }
}