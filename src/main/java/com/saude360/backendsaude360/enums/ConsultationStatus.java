package com.saude360.backendsaude360.enums;

public enum ConsultationStatus {
    SCHEDULED("Agendada"),
    CONCLUDED("Concluída"),
    CANCELED("Cancelada");


    private final String value;

    ConsultationStatus(String value) {
        this.value = value;
    }

    public String getConsultationStatusValue() {
        return value;
    }
}
