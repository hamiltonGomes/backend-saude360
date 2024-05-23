package com.saude360.backendsaude360.entities.users;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saude360.backendsaude360.dtos.ProfessionalDto;
import com.saude360.backendsaude360.entities.Clinic;
import com.saude360.backendsaude360.entities.HealthSector;
import com.saude360.backendsaude360.entities.transactions.ProfessionalTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "professionals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Professional extends User {
    @Column(unique = true, nullable = false)
    private String cnsNumber;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<ProfessionalTransaction> professionalTransactions = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "professional_health_sectors",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "health_sector_id"))
    private List<HealthSector> healthSectors = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "professionals_clinics",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id"))
    @JsonManagedReference
    private List<Clinic> clinics = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "professionals_patients",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id"))
    private List<Patient> patients = new ArrayList<>();

    public Professional(ProfessionalDto professionalDto) {
        super(
                professionalDto.fullName(), professionalDto.birthDate(), professionalDto.email(), professionalDto.phoneNumber(), professionalDto.cpf(), professionalDto.password(), professionalDto.idProfilePicture(), professionalDto.address()
        );
        this.cnsNumber = professionalDto.cnsNumber();
    }

    public void addPatient(Patient patient) {
        this.patients.add(patient);
    }

    public void addClinic(Clinic clinic) {
        this.clinics.add(clinic);
        clinic.getProfessionals().add(this);
    }

    public void removeClinic(Clinic clinic) {
        this.clinics.remove(clinic);
        clinic.getProfessionals().remove(this);
    }
}
