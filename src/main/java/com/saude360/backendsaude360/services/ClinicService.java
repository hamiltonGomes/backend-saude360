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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final ProfessionalRepository professionalRepository;
    private static final String NOT_FOUND_MESSAGE = "Clínica com ID: %d não foi encontrada.";

    @Autowired
    public ClinicService(ClinicRepository clinicRepository, ProfessionalRepository professionalRepository) {
        this.clinicRepository = clinicRepository;
        this.professionalRepository = professionalRepository;
    }

    public Clinic create(ClinicDto clinicDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var professional = professionalRepository.findByCpf(userDetails.getUsername());

        Clinic clinic = new Clinic(clinicDto);

        if(professional.getClinics().isEmpty()) {
            List<Clinic> clinics = new ArrayList<>();
            clinics.add(clinic);
            professional.setClinics(clinics);
            professionalRepository.save(professional);

            return clinicRepository.save(clinic);
        }

        professional.getClinics().add(clinic);
        professionalRepository.save(professional);

        return clinicRepository.save(clinic);
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

    public Optional<Clinic> update(Long id, ClinicDto clinicDto) {
        Optional<Clinic> optionalClinic = clinicRepository.findById(id);
        if (optionalClinic.isPresent()) {
            Clinic clinic = optionalClinic.get();
//            addProfessionalsToClinic(clinic, professionalIds);

            BeanUtils.copyProperties(clinicDto, clinic, "id");
            return Optional.of(clinicRepository.save(clinic));
        } else {
            throw new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id));
        }
    }
}