package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.PatientDto;
import com.saude360.backendsaude360.dtos.PatientFullDto;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.users.PatientRepository;
import com.saude360.backendsaude360.utils.BCryptPassword;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private static final String NOT_FOUND_MESSAGE = "Paciente com cpf %s não foi encontrado.F";

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Optional<Patient> update(PatientDto patientDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Patient> optionalPatient = patientRepository.findByCpf(userDetails.getUsername());
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            var oldPassword = patient.getPassword();
            BeanUtils.copyProperties(patientDto, patient, "professionals");
            if (patientDto.password() == null) {
                patient.setPassword(oldPassword);
            } else {
                patient.setPassword(BCryptPassword.encryptPassword(patientDto.password()));
            }
            return Optional.of(patientRepository.save(patient));
        } else {
            throw new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, userDetails.getUsername()));
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

    public List<PatientFullDto> findPatientLastConsultationAndOrientation() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var patients = patientRepository.findPatientsByProfessionalCpf(userDetails.getUsername());
        if(patients.isEmpty()) {
            throw new ObjectNotFoundException("Não foi possível encontrar pacientes para o profissional de CPF: " + userDetails.getUsername() + ".");
        }

        List<PatientFullDto> patientWithConsultations = new ArrayList<>();

        for(Patient patient : patients) {
            PatientFullDto consultationAndOrientation = patientRepository.findConsultationAndOrientationByPatientId(patient.getId(), userDetails.getUsername());

            patientWithConsultations.add(consultationAndOrientation);
        }

        return patientWithConsultations;
    }

    public List<Patient> findPatientsByProfessionalCpf() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var patients = patientRepository.findPatientsByProfessionalCpf(userDetails.getUsername());

        if(patients.isEmpty()) {
            throw new ObjectNotFoundException("Não foi possível encontrar pacientes para o profissional de CPF: " + userDetails.getUsername() + ".");
        }

        return patients;
    }
}
