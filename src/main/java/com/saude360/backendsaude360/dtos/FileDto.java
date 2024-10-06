package com.saude360.backendsaude360.dtos;

public record FileDto(
        String fileBase64,
        String type,
        Long size
) {
}
