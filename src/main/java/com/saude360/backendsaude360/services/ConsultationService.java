package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ConsultationDto;
import com.saude360.backendsaude360.dtos.ConsultationUpdateDto;
import com.saude360.backendsaude360.dtos.EvolutionHistoryDto;
import com.saude360.backendsaude360.entities.Consultation;
import com.saude360.backendsaude360.entities.EvolutionHistory;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.enums.ConsultationStatus;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.ConsultationRepository;
import com.saude360.backendsaude360.repositories.users.PatientRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
public class ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final ProfessionalRepository professionalRepository;
    private final PatientRepository patientRepository;
    private static final String NOT_FOUND_MESSAGE = "Consulta com ID: %d não foi encontrada.";

    @Autowired
    public ConsultationService(ConsultationRepository consultationRepository, ProfessionalRepository professionalRepository, PatientRepository patientRepository) {
        this.consultationRepository = consultationRepository;
        this.professionalRepository = professionalRepository;
        this.patientRepository = patientRepository;
    }

    public Consultation create(ConsultationDto consultationDto, Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Professional professional = professionalRepository.findByCpf(userDetails.getUsername());

        var patient = patientRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Paciente com o ID " + id + " não foi encontrado."));

        Consultation consultation = new Consultation(consultationDto, patient, professional);

        return consultationRepository.save(consultation);
    }

    public Consultation addEvolutionHistory(Long consultationId, EvolutionHistoryDto evolutionHistoryDto) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ObjectNotFoundException("Consulta com o ID " + consultationId + " não foi encontrada."));
        EvolutionHistory evolutionHistory = new EvolutionHistory(evolutionHistoryDto);
        consultation.setEvolutionHistory(evolutionHistory);
        return consultationRepository.save(consultation);
    }


    private Patient findPatientByName(String patientName, Professional professional) {
        List<Patient> patients = professional.getPatients();

        if (patients.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Paciente com o nome: %s não foi encontrado para o profissional de CPF: %s.", patientName, professional.getCpf()));
        }

        for (Patient patient : patients) {
            if (patient.getFullName().equals(patientName)) {
                return patient;
            }
        }

        throw new ObjectNotFoundException(String.format("Paciente com o nome: %s não foi encontrado para o profissional de CPF: %s.", patientName, professional.getCpf()));
    }


    public Consultation update(Long id, ConsultationUpdateDto consultationUpdateDto) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

        if (consultationUpdateDto.statusConsultation() != null) {
            ConsultationStatus status = ConsultationStatus.valueOf(consultationUpdateDto.statusConsultation().toUpperCase());
            consultation.setStatusConsultation(status);
        }

        if (consultationUpdateDto.date() != null) {
            consultation.setDate(consultationUpdateDto.date());
        }

        if (consultationUpdateDto.startServiceDateAndTime() != null) {
            consultation.setStartServiceDateAndTime(consultationUpdateDto.startServiceDateAndTime());
        }

        if (consultationUpdateDto.endServiceDateAndTime() != null) {
            consultation.setEndServiceDateAndTime(consultationUpdateDto.endServiceDateAndTime());
        }

        if(consultationUpdateDto.title() != null) {
            consultation.setTitle(consultationUpdateDto.title());
        }
        if(consultationUpdateDto.description() != null) {
            consultation.setDescription(consultationUpdateDto.description());
        }
        if(consultationUpdateDto.color() != null) {
            consultation.setColor(consultationUpdateDto.color());
        }

        if (consultationUpdateDto.evolutionHistory() != null) {
            EvolutionHistory evolutionHistory = new EvolutionHistory(consultationUpdateDto.evolutionHistory());
            consultation.setEvolutionHistory(evolutionHistory);
        }


        consultation.setUpdatedAt(LocalDateTime.now((ZoneId.of("America/Sao_Paulo"))));
        return consultationRepository.save(consultation);
    }

    public List<Consultation> findAll() {
        return consultationRepository.findAll();
    }

    public List<Consultation> findAllConsultationsByPatientAndProfessional(Long patientId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String professionalCpf = userDetails.getUsername();
        Professional professional = professionalRepository.findByCpf(professionalCpf);
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ObjectNotFoundException("Patient not found"));

        return consultationRepository.findAllByProfessionalAndPatient(professional, patient);
    }

    public List<Consultation> findAllConsultationsByPatient() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String patientCpf = userDetails.getUsername();
        var patient = patientRepository.findByCpf(patientCpf).orElseThrow(() -> new ObjectNotFoundException("Paciente não encontrado"));

        return consultationRepository.findAllByPatient(patient);
    }

    public Consultation findById(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
    }

    public void delete(Long id) {
        if (!consultationRepository.existsById(id)) {
            throw new ObjectNotFoundException(String.format(NOT_FOUND_MESSAGE, id));
        }
        consultationRepository.deleteById(id);
    }
}
