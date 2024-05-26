package com.saude360.backendsaude360.dtos;

import jakarta.validation.constraints.NotBlank;


public record HealthSectorDto(
        @NotBlank
        String name,
        Long professionalId
) {
}
