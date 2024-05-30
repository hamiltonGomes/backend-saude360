package com.saude360.backendsaude360.utils;

import com.saude360.backendsaude360.entities.users.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptPassword {

    private static final int passwordComplexity = 10;

    public static String encryptPassword(User user) {
        return BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(passwordComplexity));
    }
}