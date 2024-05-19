package com.saude360.backendsaude360.repositories.transactions;

import com.saude360.backendsaude360.entities.transactions.ProfessionalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalTransactionRepository extends JpaRepository<ProfessionalTransaction, Long> {
}
