package com.saude360.backendsaude360.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

public record ConsultationDto(
        @NotNull
        LocalDate date,
        @NotNull
        Instant startServiceDateAndTime,
        @NotNull
        Instant endServiceDateAndTime,
        @NotNull
        String title,
        @NotNull
        String description,
        @NotNull
        String color,
        @Valid
        EvolutionHistoryDto evolutionHistory
) {
}
