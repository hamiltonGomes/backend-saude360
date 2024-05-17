package com.saude360.backendsaude360.repositories.users;

import com.saude360.backendsaude360.entities.users.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
}
