package com.saude360.backendsaude360.dtos;

import com.saude360.backendsaude360.entities.OrientationResponse;

import java.time.ZonedDateTime;
import java.util.List;


public record OrientationWithResponsesDto(
        Long id,
        String title,
        String description,
        List<OrientationResponseReturnDto> responses
) {
}
