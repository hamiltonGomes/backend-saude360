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
    private final AddressService addressService;
    private static final String NOT_FOUND_MESSAGE = "Patient with id %s not found";

    @Autowired
    public PatientService(PatientRepository patientRepository, AddressService addressService) {
        this.patientRepository = patientRepository;
        this.addressService = addressService;
    }

    public Optional<Patient> update(Long patientId, PatientDto patientDto) {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            BeanUtils.copyProperties(patientDto, patient, "professionals");
            if (patientDto.address() != null) {
                addressService.update(patient.getAddress().getId(), patientDto.address());
            }
            return Optional.of(patientRepository.save(patient));
        } else {
            throw new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, patientId));
        }
    }
}
