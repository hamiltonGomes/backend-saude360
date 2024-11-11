package com.saude360.backendsaude360.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorRandomTests {

    @Test
    void testGenerateCodeLength() {
        // Testa se o código gerado tem o comprimento correto
        String code = GeneratorRandom.generateCode();
        assertEquals(6, code.length(), "O código gerado deve ter 6 caracteres.");
    }

    @Test
    void testGenerateCodeCharacters() {
        // Testa se o código gerado contém apenas caracteres válidos
        String code = GeneratorRandom.generateCode();
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (char c : code.toCharArray()) {
            assertTrue(validCharacters.indexOf(c) >= 0, "O código gerado deve conter apenas caracteres válidos.");
        }
    }

    @Test
    void testGenerateCodeUniqueness() {
        // Testa a aleatoriedade gerando múltiplos códigos
        String code1 = GeneratorRandom.generateCode();
        String code2 = GeneratorRandom.generateCode();

        assertNotEquals(code1, code2, "Os códigos gerados devem ser diferentes.");
    }
}
