package com.saude360.backendsaude360.entities;


import com.saude360.backendsaude360.dtos.ConsultationDto;
import com.saude360.backendsaude360.entities.users.Patient;
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
    private ConsultationStatus statusConsultation = ConsultationStatus.SCHEDULED;

    @Column(nullable = false)
    private Instant startServiceDateAndTime;

    @Column(nullable = false)
    private Instant endServiceDateAndTime;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evolution_history_id")
    private EvolutionHistory evolutionHistory;

    public Consultation(ConsultationDto consultationDto, Patient patient, EvolutionHistory evolutionHistory) {
        this.date = consultationDto.date();
        this.startServiceDateAndTime = consultationDto.startServiceDateAndTime();
        this.endServiceDateAndTime = consultationDto.endServiceDateAndTime();
        this.evolutionHistory = evolutionHistory;
        this.patient = patient;
    }
}
