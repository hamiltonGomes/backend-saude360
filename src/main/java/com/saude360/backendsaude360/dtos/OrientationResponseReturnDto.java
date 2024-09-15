package com.saude360.backendsaude360.dtos;

import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.users.User;


import java.time.LocalDateTime;
import java.util.List;

public record OrientationResponseReturnDto (
        String content,
        Orientation orientation,
        List<String> images,
        User user,
        LocalDateTime createdAt
){
}
