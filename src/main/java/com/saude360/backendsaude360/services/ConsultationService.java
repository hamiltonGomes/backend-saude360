package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.ConsultationDto;
import com.saude360.backendsaude360.dtos.ConsultationUpdateDto;
import com.saude360.backendsaude360.entities.Consultation;
import com.saude360.backendsaude360.entities.EvolutionHistory;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.enums.ConsultationStatus;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.ConsultationRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final ProfessionalRepository professionalRepository;
    private static final String NOT_FOUND_MESSAGE = "Consulta com ID: %d não foi encontrada.";

    @Autowired
    public ConsultationService(ConsultationRepository consultationRepository, ProfessionalRepository professionalRepository) {
        this.consultationRepository = consultationRepository;
        this.professionalRepository = professionalRepository;
    }

    public Consultation create(ConsultationDto consultationDto) {
        String patientName = consultationDto.patientName();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String professionalCpf = userDetails.getUsername();
        Professional professional = professionalRepository.findByCpf(professionalCpf);

        List<Patient> patients = professional.getPatients();

        if (patients.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Paciente com o nome: %s não foi encontrado para o profissional de CPF: %s.", patientName, professionalCpf));
        }

        Patient patient = patients.get(0);
        EvolutionHistory evolutionHistory = new EvolutionHistory(consultationDto.evolutionHistory());
        Consultation consultation = new Consultation(consultationDto, patient, evolutionHistory);
        return consultationRepository.save(consultation);
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

        if (consultationUpdateDto.patientName() != null) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String professionalCpf = userDetails.getUsername();
            Professional professional = professionalRepository.findByCpf(professionalCpf);

            List<Patient> patients = professional.getPatients();

            if (patients.isEmpty()) {
                throw new ObjectNotFoundException(String.format("Paciente com o nome: %s não foi encontrado para o profissional de CPF: %s.", consultationUpdateDto.patientName(), professionalCpf));
            }

            Patient patient = patients.get(0);
            consultation.setPatient(patient);
        }

        if (consultationUpdateDto.evolutionHistory() != null) {
            EvolutionHistory evolutionHistory = new EvolutionHistory(consultationUpdateDto.evolutionHistory());
            consultation.setEvolutionHistory(evolutionHistory);
        }

        return consultationRepository.save(consultation);
    }

    public List<Consultation> findAll() {
        return consultationRepository.findAll();
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
