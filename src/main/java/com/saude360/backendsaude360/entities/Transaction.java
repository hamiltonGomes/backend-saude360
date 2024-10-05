package com.saude360.backendsaude360.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saude360.backendsaude360.dtos.TransactionDto;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.enums.PaymentMethod;
import com.saude360.backendsaude360.enums.PaymentStatus;
import com.saude360.backendsaude360.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    @JsonManagedReference
    private Professional professional;

    public Transaction(TransactionDto transactionDto, Professional professional) {
        this.name = transactionDto.name();
        this.value = transactionDto.value();
        this.transactionType = transactionDto.transactionType();
        this.date = transactionDto.date();
        this.paymentMethod = transactionDto.paymentMethod();
        this.paymentStatus = transactionDto.paymentStatus();
        this.professional = professional;
    }
}
