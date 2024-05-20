package com.saude360.backendsaude360.entities.transactions;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saude360.backendsaude360.entities.users.Professional;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(value = "E")
@EqualsAndHashCode(callSuper = true)
public class ProfessionalTransaction extends Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    @JsonManagedReference
    private Professional professional;
}
