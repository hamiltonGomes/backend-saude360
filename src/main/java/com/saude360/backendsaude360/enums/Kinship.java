package com.saude360.backendsaude360.enums;

public enum Kinship {
    MOTHER("Mãe"),
    FATHER("Pai"),
    GRANDMOTHER("Avó"),
    GRANDFATHER("Avô"),
    AUNT("Tia"),
    UNCLE("Tio"),
    OTHER("Outro");

    private final String value;

    Kinship(String value) {
        this.value = value;
    }

    public String getKinshipValue() {
        return value;
    }
}
