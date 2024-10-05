package com.saude360.backendsaude360.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordDto(
        @NotNull
        @NotBlank
        String code,

        @NotNull
        @NotBlank
        String password
) {
}
