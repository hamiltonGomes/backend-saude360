package com.saude360.backendsaude360.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ClinicDto(
        @NotBlank
        String cnpj,
        @NotBlank
        String phoneNumber,
        String telephoneNumber,
        @NotBlank
        String cnesNumber,
        @Valid
        AddressDto address
) {
}
