package com.saude360.backendsaude360.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saude360.backendsaude360.dtos.ConsultationDto;
import com.saude360.backendsaude360.entities.users.Patient;
import com.saude360.backendsaude360.entities.users.Professional;
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
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatus statusConsultation = ConsultationStatus.SCHEDULED;

    @Column(nullable = false)
    private Instant startServiceDateAndTime;

    @Column(nullable = false)
    private Instant endServiceDateAndTime;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    @JsonManagedReference
    @JsonIgnore
    private Professional professional;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evolution_history_id")
    private EvolutionHistory evolutionHistory;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        updatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    public Consultation(ConsultationDto consultationDto, Patient patient, EvolutionHistory evolutionHistory, Professional professional) {
        this.date = consultationDto.date();
        this.startServiceDateAndTime = consultationDto.startServiceDateAndTime();
        this.endServiceDateAndTime = consultationDto.endServiceDateAndTime();
        this.evolutionHistory = evolutionHistory;
        this.patient = patient;
        this.professional = professional;
    }

    public Consultation(ConsultationDto consultationDto, Patient patient, Professional professional) {
        this.date = consultationDto.date();
        this.startServiceDateAndTime = consultationDto.startServiceDateAndTime();
        this.endServiceDateAndTime = consultationDto.endServiceDateAndTime();
        this.title = consultationDto.title();
        this.description = consultationDto.description();
        this.color = consultationDto.color();
        this.patient = patient;
        this.professional = professional;
    }
}
