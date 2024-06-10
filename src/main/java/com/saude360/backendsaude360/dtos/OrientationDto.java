package com.saude360.backendsaude360.dtos;

import java.util.List;

public record OrientationDto(
        String title,
        String description,
        Boolean completed,
        List<String> idImages
) {
}
