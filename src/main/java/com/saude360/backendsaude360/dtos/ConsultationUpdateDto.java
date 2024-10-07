package com.saude360.backendsaude360.dtos;

import java.time.Instant;
import java.time.LocalDate;

public record ConsultationUpdateDto(
        String statusConsultation,
        LocalDate date,
        Instant startServiceDateAndTime,
        Instant endServiceDateAndTime,
        String title,
        String description,
        String color,
        EvolutionHistoryDto evolutionHistory
) {
}
