package com.saude360.backendsaude360.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saude360.backendsaude360.dtos.PatientDto;
import com.saude360.backendsaude360.entities.Address;
import com.saude360.backendsaude360.entities.Consultation;
import com.saude360.backendsaude360.entities.Orientation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
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
    private List<String> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "patients")
    @JsonIgnore
    private List<Professional> professionals = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "patients_responsible_persons",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "responsible_people_id"))
    @JsonIgnore
    private List<ResponsiblePerson> responsiblePeople = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Consultation> consultations = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Orientation> orientations = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @JsonManagedReference
    private Address address;

    public Patient(PatientDto patientDto, Professional professional) {
        super(
                patientDto.fullName(), patientDto.birthDate(), patientDto.email(), patientDto.phoneNumber(), patientDto.cpf(), null, patientDto.idProfilePicture()
        );
        this.comments.add(patientDto.comments());
        this.address = new Address(patientDto.address());
        this.password = generateRandomPassword();
        this.professionals.add(professional);
        professional.addPatient(this);
    }

    public String generateRandomPassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);

        StringBuilder rawPassword = new StringBuilder();
        for (byte b : randomBytes) {
            rawPassword.append(String.format("%02x", b));
        }
        return passwordEncoder.encode(rawPassword.toString());
    }
}
