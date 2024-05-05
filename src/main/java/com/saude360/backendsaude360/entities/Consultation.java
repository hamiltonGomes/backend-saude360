package com.saude360.backendsaude360.entities;


import com.saude360.backendsaude360.enums.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "consultations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false)
    private LocalDate date;

    @Column(unique = false, nullable = false)
    private ConsultationStatus statusConsultation;

    @Column(unique = false, nullable = false)
    private LocalTime startServiceTime;

    @Column(unique = false, nullable = false)
    private LocalTime endServiceTime;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public static Duration calculateDuration(LocalTime startServiceTime, LocalTime endServiceTime) {
        return Duration.between(startServiceTime, endServiceTime);
    }
}
