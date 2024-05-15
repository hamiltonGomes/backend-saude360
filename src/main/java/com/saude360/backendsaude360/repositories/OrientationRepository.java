package com.saude360.backendsaude360.repositories;

import com.saude360.backendsaude360.entities.Orientation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrientationRepository extends JpaRepository<Orientation, Long> {
}
