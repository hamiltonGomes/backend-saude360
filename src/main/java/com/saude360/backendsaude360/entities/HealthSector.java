package com.saude360.backendsaude360.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "health_sectors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthSector implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "healthSectors")
    @JsonIgnore
    private List<Professional> professionals = new ArrayList<>();
}
