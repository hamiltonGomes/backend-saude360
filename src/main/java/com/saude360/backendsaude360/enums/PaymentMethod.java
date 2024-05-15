package com.saude360.backendsaude360.enums;

public enum PaymentMethod {
    PIX("Pix"),
    CREDIT_CARD("Cartão de crédito"),
    DEBIT_CARD("Cartão de débito"),
    CASH("Dinheiro");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getPaymentMethod() {
        return value;
    }
}
