package com.saude360.backendsaude360.utils;

import java.security.SecureRandom;

public class GeneratorRandom {

    private static final int SEQUENCE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateCode() {
        StringBuilder sequence = new StringBuilder(SEQUENCE_LENGTH);
        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sequence.append(CHARACTERS.charAt(index));
        }
        return sequence.toString();
    }
}
