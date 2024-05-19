package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.PatientDto;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.users.PatientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Optional<Patient> update(Long id, PatientDto patientDto) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            BeanUtils.copyProperties(patientDto, patient);
            return Optional.of(patientRepository.save(patient));
        } else {
            throw new ObjectNotFoundException("Patient with ID: " + id + " was not found.");
        }
    }
}
