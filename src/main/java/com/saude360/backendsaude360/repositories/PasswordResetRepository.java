package com.saude360.backendsaude360.repositories;

import com.saude360.backendsaude360.entities.PasswordReset;
import com.saude360.backendsaude360.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    PasswordReset findByCode(String code);
    PasswordReset findByUser(User user);
}
