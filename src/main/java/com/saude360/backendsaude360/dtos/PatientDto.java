package com.saude360.backendsaude360.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record PatientDto(
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
        @Pattern(regexp = "^\\(\\d{2}\\)9\\d{4}-\\d{4}$", message = "The phone number must be in the format: " +
                "(dd)9xxxx-xxxx (Numbers only). It should contain 11 digits (DDD, 9 in front, and the number itself).")
        String phoneNumber,
        String idProfilePicture,
        @Valid
        AddressDto address,
        String comments
) {
}
