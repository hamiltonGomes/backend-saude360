package com.saude360.backendsaude360.entities.users;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saude360.backendsaude360.dtos.AddressDto;
import com.saude360.backendsaude360.entities.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @Column(nullable = false)
    protected String fullName;

    @Column(nullable = false)
    protected LocalDate birthDate;

    @Column(unique = true, nullable = false)
    protected String email;

    @Column(unique = true, nullable = false)
    protected String phoneNumber;

    @Column(unique = true, nullable = false)
    protected String cpf;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String password;

    protected Integer idProfilePicture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @JsonManagedReference
    protected Address address;

    public User(String fullName, LocalDate birthDate, String email, String phoneNumber, String cpf, String password, Integer idProfilePicture, AddressDto addressDto) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cpf = cpf;
        this.password = password;
        this.idProfilePicture = idProfilePicture;
        this.address = new Address(addressDto);
    }
}
