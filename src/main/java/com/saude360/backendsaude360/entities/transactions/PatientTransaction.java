package com.saude360.backendsaude360.entities.transactions;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saude360.backendsaude360.entities.users.Patient;
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
@DiscriminatorValue(value = "I")
@EqualsAndHashCode(callSuper = true)
public class PatientTransaction extends Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonManagedReference
    private Patient patient;
}
