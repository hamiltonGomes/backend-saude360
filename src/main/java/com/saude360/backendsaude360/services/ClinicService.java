package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ClinicDto;
import com.saude360.backendsaude360.entities.Clinic;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.ClinicRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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

    // FIXME: ajustar a atualização da cliníca com os ids dos profissionais, é retirar a clínica do profissional antigo e colocar no novo, ou colocar um profissional novo a uma clínica que já possui um profisisonal
    private void addProfessionalsToClinic(Clinic clinic, List<Long> professionalIds) {
        clinic.getProfessionals().removeIf(professional -> !professionalIds.contains(professional.getId()));

        for (Long professionalId : professionalIds) {
            Professional professional = professionalRepository.findById(professionalId)
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Professional with ID: %d was not found.", professionalId)));
            if (!clinic.getProfessionals().contains(professional)) {
                clinic.getProfessionals().add(professional);
                professional.getClinics().add(clinic);
            }
        }
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

        for (Professional professional : clinic.getProfessionals()) {
            professional.getClinics().remove(clinic);
        }
        clinic.getProfessionals().clear();
        clinicRepository.save(clinic);
        clinicRepository.deleteById(clinicId);
    }

    public Optional<Clinic> update(Long id, ClinicDto clinicDto, List<Long> professionalIds) {
        Optional<Clinic> optionalClinic = clinicRepository.findById(id);
        if (optionalClinic.isPresent()) {
            Clinic clinic = optionalClinic.get();
            BeanUtils.copyProperties(clinicDto, clinic);

            // Clear the current professionals and add the updated ones
            clinic.getProfessionals().clear();
            addProfessionalsToClinic(clinic, professionalIds);

            return Optional.of(clinicRepository.save(clinic));
        } else {
            throw new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id));
        }
    }
}
