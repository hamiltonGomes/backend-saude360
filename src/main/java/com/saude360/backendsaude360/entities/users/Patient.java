package com.saude360.backendsaude360.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saude360.backendsaude360.entities.Consultation;
import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.transactions.PatientTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Patient extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String comments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientTransaction> patientTransactions;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Consultation> consultations = new ArrayList<>();
    // FIXME: change to many to many
    @ManyToOne
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @ManyToMany
    @JoinTable(name = "patients_responsible_persons",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "responsible_people_id"))
    private List<ResponsiblePerson> responsiblePeople = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Orientation> orientations = new ArrayList<>();
}