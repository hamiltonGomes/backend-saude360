package com.saude360.backendsaude360.repositories;

import com.saude360.backendsaude360.entities.Consultation;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.entities.users.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findAllByProfessionalAndPatient(Professional professional, Patient patient);

    List<Consultation> findAllByPatient(Patient patient);
}
