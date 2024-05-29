package com.saude360.backendsaude360.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserAuthenticationDto(
        @NotBlank
        String cpf,
        @NotBlank
        String password
) {
}
