package com.saude360.backendsaude360.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saude360.backendsaude360.dtos.ClinicDto;
import com.saude360.backendsaude360.entities.users.Professional;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clinics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clinic implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cnpj;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String telephoneNumber;

    @Column(unique = true, nullable = false)
    private String cnesNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @JsonManagedReference
    private Address address;

    @ManyToMany(mappedBy = "clinics")
    @JsonBackReference
    private List<Professional> professionals = new ArrayList<>();

    public Clinic(ClinicDto clinicDto) {
        this.cnesNumber = clinicDto.cnesNumber();
        this.cnpj = clinicDto.cnpj();
        this.phoneNumber = clinicDto.phoneNumber();
        this.telephoneNumber = clinicDto.telephoneNumber();
        this.address = new Address(clinicDto.address());
    }

    public void addProfessional(Professional professional) {
        this.professionals.add(professional);
        professional.getClinics().add(this);
    }

    public void removeProfessional(Professional professional) {
        this.professionals.remove(professional);
        professional.getClinics().remove(this);
    }
}
