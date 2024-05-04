package com.saude360.backendsaude360.entities;

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
    private long id;

    @Column(unique = false, nullable = false)
    private String cep;

    @Column(unique = false, nullable = false)
    private String neighborhood;

    @Column(unique = false, nullable = false)
    private String city;

    @Column(unique = false, nullable = false)
    private String state;

    @Column(unique = false, nullable = true)
    private int number;

    @Column(unique = false, nullable = true)
    private String complement;

    @OneToOne(mappedBy = "address")
    private User user;

    @OneToOne(mappedBy = "address")
    private Clinic clinic;
}
