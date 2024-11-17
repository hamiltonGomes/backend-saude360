package com.saude360.backendsaude360.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record ProfessionalDto(
        @NotBlank
        String fullName,
        @NotBlank
        String cpf,
        @NotNull
        LocalDate birthDate,
        @Email
        @NotBlank
        String email,
        @NotNull
        @Pattern(regexp = "^\\(\\d{2}\\)9\\d{4}-\\d{4}$", message = "O número de telefone deve ser nesse formato: " +
                "(dd)9xxxx-xxxx (Números somente).")
        String phoneNumber,
        @Pattern(regexp = "^[\\s\\S]{1,16}$",
                message = "A string deve conter entre 1 e 16 caracteres.")
        String password,
        String idProfilePicture,
        @NotBlank
        String cnsNumber,
        @NotEmpty
        List<String> healthSectorsNames,
        List<ClinicDto> clinic
) {
}
