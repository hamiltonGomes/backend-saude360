package com.saude360.backendsaude360.repositories;

import com.saude360.backendsaude360.entities.HealthSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthSectorRepository extends JpaRepository<HealthSector, Long> {
    Optional<HealthSector> findByName(String name);
    boolean existsByName(String name);
}
