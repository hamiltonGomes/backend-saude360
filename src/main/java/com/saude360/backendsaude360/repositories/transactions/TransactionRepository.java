package com.saude360.backendsaude360.repositories.transactions;

import com.saude360.backendsaude360.entities.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
