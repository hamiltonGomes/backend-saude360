package com.saude360.backendsaude360.entities.users;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saude360.backendsaude360.dtos.PatientDto;
import com.saude360.backendsaude360.entities.Consultation;
import com.saude360.backendsaude360.entities.Orientation;
import com.saude360.backendsaude360.entities.transactions.PatientTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Patient extends User {
    private List<String> comments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<PatientTransaction> patientTransactions;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Consultation> consultations = new ArrayList<>();

    @ManyToMany(mappedBy = "patients")
    @JsonIgnore
    private List<Professional> professionals;

    @ManyToMany
    @JoinTable(name = "patients_responsible_persons",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "responsible_people_id"))
    @JsonIgnore
    private List<ResponsiblePerson> responsiblePeople = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Orientation> orientations = new ArrayList<>();

    public Patient(PatientDto patientDto) {
        super(
                patientDto.fullName(), patientDto.birthDate(), patientDto.email(), patientDto.phoneNumber(), patientDto.cpf(), patientDto.password(), patientDto.idProfilePicture(), patientDto.address()
        );
    }
}
