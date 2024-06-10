package com.saude360.backendsaude360.repositories;

import com.saude360.backendsaude360.entities.OrientationResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrientationResponseRepository extends JpaRepository<OrientationResponse, Long> {

    List<OrientationResponse> findAllByOrientationId(Long orientationId);

}
