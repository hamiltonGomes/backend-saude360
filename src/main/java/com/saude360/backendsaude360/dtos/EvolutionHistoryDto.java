package com.saude360.backendsaude360.dtos;

import jakarta.validation.constraints.NotBlank;

public record EvolutionHistoryDto(
        @NotBlank
        String sessionResume,
        @NotBlank
        String nextSteps
) {
}
