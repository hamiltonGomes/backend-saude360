package com.saude360.backendsaude360.dtos;

import jakarta.validation.constraints.*;

import java.util.List;

public record ProfessionalUpdateDto(
        String fullName,
        @Pattern(regexp = "^\\(\\d{2}\\)9\\d{4}-\\d{4}$", message = "O número de telefone deve ser nesse formato: " +
                "(dd)9xxxx-xxxx (Números somente).")
        String phoneNumber,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[0-9A-Za-z]{1,16}$",
                message = "The string must contain exactly 16 alphanumeric characters with at least one uppercase letter.")
        String password,
        String idProfilePicture,
        String cnsNumber,
        List<String> healthSectorsNames,
        List<ClinicUpdateDto> clinic
) {
}
