package com.saude360.backendsaude360.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

public record ConsultationDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        @NotNull
        LocalDate date,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        @NotNull
        Instant startServiceDateAndTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
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
