package com.saude360.backendsaude360.dtos;

import java.util.List;


public record OrientationWithResponsesDto(
        Long id,
        String title,
        String description,
        List<OrientationResponseReturnDto> responses
) {
}
