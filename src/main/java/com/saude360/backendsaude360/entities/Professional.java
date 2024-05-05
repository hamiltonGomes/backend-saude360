package com.saude360.backendsaude360.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
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
public class Professional extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false)
    private String cnsNumber;

    @ManyToMany
    @JoinTable(name = "professional_health_sectors",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "health_sector_id"))
    private Set<HealthSector> healthSectors = new HashSet<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Clinic> clinics = new ArrayList<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Patient> patients = new ArrayList<>();
}
