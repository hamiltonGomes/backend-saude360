package com.saude360.backendsaude360.repositories.users;

import com.saude360.backendsaude360.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCpf(String cpf);
}
