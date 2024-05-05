package com.saude360.backendsaude360.enums;

public enum PaymentMethod {
    PIX("Pix"),
    CREDIT_CARD("Credit card"),
    DEBIT_CARD("Debit card"),
    CASH("Cash");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getPaymentMethod() {
        return value;
    }
}
