package com.saude360.backendsaude360.entities;


import com.saude360.backendsaude360.enums.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;

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
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatus statusConsultation;

    @Column(nullable = false)
    private Instant startServiceTime;

    @Column(nullable = false)
    private Instant endServiceTime;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evolution_history_id")
    private EvolutionHistory evolutionHistory;

    public static Duration calculateDuration(LocalTime startServiceTime, LocalTime endServiceTime) {
        return Duration.between(startServiceTime, endServiceTime);
    }
}
