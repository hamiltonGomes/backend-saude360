package com.saude360.backendsaude360.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saude360.backendsaude360.enums.Kinship;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "responsible_persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class ResponsiblePerson extends User {
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Kinship kinship;

    @ManyToMany(mappedBy = "responsiblePeople")
    @JsonIgnore
    private List<Patient> patients = new ArrayList<>();
}
