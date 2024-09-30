package com.saude360.backendsaude360.controllers;

import com.saude360.backendsaude360.dtos.TransactionDto;
import com.saude360.backendsaude360.entities.Transaction;
import com.saude360.backendsaude360.services.TransactionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<Transaction> create(@RequestBody @Valid TransactionDto transactionDto) {
        Transaction transaction = transactionService.create(transactionDto);
        return ResponseEntity.ok().body(transaction);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Transaction> update(@PathVariable Long id, @RequestBody @Valid TransactionDto transactionDto) {
        Transaction transaction = transactionService.update(id, transactionDto).orElseThrow();
        return ResponseEntity.ok().body(transaction);
    }

    @GetMapping("/")
    public ResponseEntity<List<Transaction>> findAll() {
        List<Transaction> transactions = transactionService.findAll();
        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable Long id) {
        Transaction transaction = transactionService.findById(id).orElseThrow();
        return ResponseEntity.ok().body(transaction);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}