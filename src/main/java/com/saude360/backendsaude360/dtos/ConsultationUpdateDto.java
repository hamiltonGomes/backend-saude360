package com.saude360.backendsaude360.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.time.LocalDate;

public record ConsultationUpdateDto(
        String statusConsultation,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        LocalDate date,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        Instant startServiceDateAndTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        Instant endServiceDateAndTime,
        String title,
        String description,
        String color,
        EvolutionHistoryDto evolutionHistory
) {
}
