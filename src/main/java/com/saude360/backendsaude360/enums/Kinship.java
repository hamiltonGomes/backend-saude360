package com.saude360.backendsaude360.enums;

public enum Kinship {
    MOTHER("Mother"),
    FATHER("Father"),
    GRANDMOTHER("Grandmother"),
    GRANDFATHER("Grandfather"),
    AUNT("Aunt"),
    UNCLE("Uncle"),
    OTHER("Other");

    private final String value;

    Kinship(String value) {
        this.value = value;
    }

    public String getKinshipValue() {
        return value;
    }
}
