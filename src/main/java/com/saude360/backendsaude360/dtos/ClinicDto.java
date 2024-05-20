package com.saude360.backendsaude360.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ClinicDto(
        @NotBlank
        String cnpj,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String telephoneNumber,
        @NotBlank
        String cnesNumber,
        @Valid
        AddressDto addressdto,
        @NotEmpty
        List<Long> professionalIds
) {
}