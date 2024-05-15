package com.saude360.backendsaude360.enums;

public enum TransactionType {
    INCOME("Entrada"),
    EXPENSE("Saída");

    public final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getTransactionType() {
        return value;
    }
}
