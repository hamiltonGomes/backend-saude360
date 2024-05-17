package com.saude360.backendsaude360.entities;

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
    private Boolean completed;
    private List<String> idImages; //check this type with Igor

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public boolean addImage(String idImage) {
        return idImages.add(idImage);
    }

    public boolean removeImage(String idImage) {
        return idImages.remove(idImage);
    }
}
