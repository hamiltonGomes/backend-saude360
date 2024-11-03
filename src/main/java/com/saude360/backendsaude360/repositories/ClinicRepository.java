package com.saude360.backendsaude360.repositories;

import com.saude360.backendsaude360.entities.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findByCnpj(String cnpj);
}