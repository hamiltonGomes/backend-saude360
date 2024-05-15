package com.saude360.backendsaude360.enums;

public enum PaymentStatus {
    PENDING("Pendente"),
    CONCLUDED("Concluído"),
    LATE("Atrasado");

    public final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getPaymentStatus() {
        return value;
    }
}
