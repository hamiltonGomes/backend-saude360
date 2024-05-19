package com.saude360.backendsaude360.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.saude360.backendsaude360.dtos.AddressDto;
import com.saude360.backendsaude360.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String street;

    private Integer number;

    private String complement;

    @OneToOne(mappedBy = "address")
    @JsonBackReference
    private User user;

    @OneToOne(mappedBy = "address")
    @JsonBackReference
    private Clinic clinic;

    public Address(AddressDto addressDto) {
        this.cep = addressDto.cep();
        this.state = addressDto.state();
        this.city = addressDto.city();
        this.neighborhood = addressDto.neighborhood();
        this.street = addressDto.street();
        this.number = addressDto.number();
        this.complement = addressDto.complement();
    }
}
