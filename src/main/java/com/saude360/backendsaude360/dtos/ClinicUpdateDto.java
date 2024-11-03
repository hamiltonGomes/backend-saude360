package com.saude360.backendsaude360.dtos;

public record ClinicUpdateDto(
        String cnpj,
        String phoneNumber,
        String telephoneNumber,
        String cnesNumber,
        AddressUpdateDto address
) {
}
