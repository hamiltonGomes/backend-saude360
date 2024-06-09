package com.saude360.backendsaude360.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.ZonedDateTime;

public record ConsultationUpdateDto(
        String statusConsultation,
        ZonedDateTime date,
        Instant startServiceDateAndTime,
        Instant endServiceDateAndTime,
        String title,
        String description,
        String color,
        EvolutionHistoryDto evolutionHistory
) {
}
