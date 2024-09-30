package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.TransactionDto;
import com.saude360.backendsaude360.entities.Transaction;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import com.saude360.backendsaude360.repositories.TransactionRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProfessionalRepository professionalRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, ProfessionalRepository professionalRepository) {
        this.transactionRepository = transactionRepository;
        this.professionalRepository = professionalRepository;
    }

    public Transaction create(TransactionDto transactionDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Professional professional = professionalRepository.findByCpf(userDetails.getUsername());

        Transaction transaction = new Transaction(transactionDto, professional);
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> findById(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);

        return Optional.ofNullable(transaction.orElseThrow(() -> new ObjectNotFoundException("Transaction with ID: " + id + " not found.")));
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> update(Long id, TransactionDto transactionDto) {
        return transactionRepository.findById(id).map(transaction -> {

            if (transactionDto.name() != null) {
                transaction.setName(transactionDto.name());
            }
            if (transactionDto.value() != null) {
                transaction.setValue(transactionDto.value());
            }
            if (transactionDto.transactionType() != null) {
                transaction.setTransactionType(transactionDto.transactionType());
            }
            if (transactionDto.date() != null) {
                transaction.setDate(transactionDto.date());
            }
            if (transactionDto.paymentMethod() != null) {
                transaction.setPaymentMethod(transactionDto.paymentMethod());
            }
            if (transactionDto.paymentStatus() != null) {
                transaction.setPaymentStatus(transactionDto.paymentStatus());
            }
            return transactionRepository.save(transaction);
        });
    }

    public void delete(Long id) {
        try {
            transactionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Transaction with ID: " + id + " not found.");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}