package com.saude360.backendsaude360.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saude360.backendsaude360.dtos.EvolutionHistoryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvolutionHistory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "evolutionHistory")
    @JsonIgnore
    private Consultation consultation;

    private String sessionResume;

    private String nextSteps;

    public EvolutionHistory(EvolutionHistoryDto evolutionHistoryDto) {
        this.sessionResume = evolutionHistoryDto.sessionResume();
        this.nextSteps = evolutionHistoryDto.nextSteps();
    }
}
