package com.saude360.backendsaude360.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saude360.backendsaude360.dtos.OrientationDto;
import com.saude360.backendsaude360.entities.users.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "orientations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orientation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Boolean completed = false;
    private List<String> idImages;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Orientation(OrientationDto orientationDto) {
        this.description = orientationDto.description();
        this.completed = orientationDto.completed();
    }

    public boolean addImage(String idImage) {
        return idImages.add(idImage);
    }

    public boolean removeImage(String idImage) {
        return idImages.remove(idImage);
    }
}
