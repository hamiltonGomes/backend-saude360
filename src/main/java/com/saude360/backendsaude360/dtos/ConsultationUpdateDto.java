package com.saude360.backendsaude360.dtos;

import java.time.Instant;
import java.time.ZonedDateTime;

public record ConsultationUpdateDto(
        String statusConsultation,
        ZonedDateTime date,
        Instant startServiceDateAndTime,
        Instant endServiceDateAndTime,
        String patientName,
        EvolutionHistoryDto evolutionHistory
) {
}
