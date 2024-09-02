package com.saude360.backendsaude360.dtos;

import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.users.User;


import java.time.LocalDateTime;

public record OrientationResponseReturnDto (
        String content,
        String imageBase64,
        Orientation orientation,
        String filePath,
        User user,
        LocalDateTime createdAt
){
}
