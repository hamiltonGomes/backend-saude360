package com.saude360.backendsaude360.repositories;

import com.saude360.backendsaude360.entities.HealthSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthSectorRepository extends JpaRepository<HealthSector, Long> {
    HealthSector findByName(String name);

    boolean existsByName(String name);
}
