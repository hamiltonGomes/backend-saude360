package com.saude360.backendsaude360.entities.users;

import com.saude360.backendsaude360.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRoles name;
}
