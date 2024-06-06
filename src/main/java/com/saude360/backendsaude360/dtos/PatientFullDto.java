package com.saude360.backendsaude360.dtos;

import java.time.Instant;
import java.time.ZonedDateTime;

public record PatientFullDto(

        String paciente,
        Instant dataConsulta,
        ZonedDateTime ultimoEnvio,
        ZonedDateTime ultimoFeedback) {
}
