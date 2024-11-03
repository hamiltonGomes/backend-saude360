package com.saude360.backendsaude360.dtos;

public record AddressUpdateDto(
        String cep,
        String state,
        String city,
        String neighborhood,
        String street,
        Integer number,
        String complement
) {
}
