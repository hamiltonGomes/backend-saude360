package com.saude360.backendsaude360.dtos;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank
        String cep,
        @NotBlank
        String state,
        @NotBlank
        String city,
        @NotBlank
        String neighborhood,
        @NotBlank
        String street,
        Integer number,
        String complement
) {
}
