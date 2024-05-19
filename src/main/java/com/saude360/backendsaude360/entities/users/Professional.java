package com.saude360.backendsaude360.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saude360.backendsaude360.entities.Clinic;
import com.saude360.backendsaude360.entities.HealthSector;
import com.saude360.backendsaude360.entities.transactions.ProfessionalTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private List<ProfessionalTransaction> professionalTransactions;

    @ManyToMany
    @JoinTable(name = "professional_health_sectors",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "health_sector_id"))
    private Set<HealthSector> healthSectors = new HashSet<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private List<Clinic> clinics = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "professionals_patients",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id"))
    private List<Patient> patients = new ArrayList<>();
}
