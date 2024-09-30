package com.saude360.backendsaude360.dtos;

import com.saude360.backendsaude360.enums.PaymentMethod;
import com.saude360.backendsaude360.enums.PaymentStatus;
import com.saude360.backendsaude360.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TransactionDto(
        @NotBlank
        String name,
        @NotNull
        BigDecimal value,
        @NotNull
        TransactionType transactionType,
        @NotNull
        ZonedDateTime date,
        @NotNull
        PaymentMethod paymentMethod,
        @NotNull
        PaymentStatus paymentStatus
) {
}