package com.saude360.backendsaude360.repositories;

import com.saude360.backendsaude360.entities.Transaction;
import com.saude360.backendsaude360.entities.users.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByProfessional(Professional professional);
}
