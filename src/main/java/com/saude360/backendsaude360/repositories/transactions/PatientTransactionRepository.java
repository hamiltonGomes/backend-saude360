package com.saude360.backendsaude360.repositories.transactions;

import com.saude360.backendsaude360.entities.transactions.PatientTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientTransactionRepository extends JpaRepository<PatientTransaction, Long> {
}
