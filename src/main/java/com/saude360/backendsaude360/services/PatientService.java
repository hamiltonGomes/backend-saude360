package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.PatientDto;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.users.PatientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AddressService addressService;
    private static final String NOT_FOUND_MESSAGE = "Paciente com id %s não foi encontrado.F";

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

    public Patient findById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public void delete(Long id) {
        try {
            patientRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format(String.format(NOT_FOUND_MESSAGE, id)));
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
