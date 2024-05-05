package com.saude360.backendsaude360.enums;

public enum PaymentStatus {
    PENDING("Pending"),
    CONCLUDED("Concluded"),
    LATE("Late");

    public final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getPaymentStatus() {
        return value;
    }
}
