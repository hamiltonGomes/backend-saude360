package com.saude360.backendsaude360.utils;

import com.saude360.backendsaude360.entities.users.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptPassword {

    private static final int PASSWORD_COMPLEXITY = 10;

    private BCryptPassword() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static String encryptPassword(User user) {
        return BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(PASSWORD_COMPLEXITY));
    }

    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(PASSWORD_COMPLEXITY));
    }
}