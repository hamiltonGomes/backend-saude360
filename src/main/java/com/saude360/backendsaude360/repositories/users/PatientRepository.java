package com.saude360.backendsaude360.repositories.users;

import com.saude360.backendsaude360.dtos.PatientFullDto;
import com.saude360.backendsaude360.entities.users.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p JOIN p.professionals prof WHERE prof.cpf = ?1")
    List<Patient> findPatientsByProfessionalCpf(String cpf);

    @Query(value = "SELECT new com.saude360.backendsaude360.dtos.PatientFullDto(p.id, p.fullName, c.startServiceDateAndTime, o.createdAt, o.updatedAt) "
    + "FROM Patient p LEFT JOIN p.consultations c LEFT JOIN p.orientations o "
    + "WHERE p.id = ?1 AND c.professional.cpf = ?2 "
    + "ORDER BY c.startServiceDateAndTime DESC")
    PatientFullDto findConsultationAndOrientationByPatientId(Long patientId, String profissional);
}