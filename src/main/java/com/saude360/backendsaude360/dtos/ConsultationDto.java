package com.saude360.backendsaude360.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.ZonedDateTime;

public record ConsultationDto(
        @NotNull
        ZonedDateTime date,
        @NotNull
        Instant startServiceDateAndTime,
        @NotNull
        Instant endServiceDateAndTime,
        @NotNull
        String patientName,
        @Valid
        EvolutionHistoryDto evolutionHistory
) {
}
