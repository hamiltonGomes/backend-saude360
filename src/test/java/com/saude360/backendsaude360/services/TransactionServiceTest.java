package com.saude360.backendsaude360.services;

import com.saude360.backendsaude360.dtos.TransactionDto;
import com.saude360.backendsaude360.entities.Transaction;
import com.saude360.backendsaude360.entities.users.Professional;
import com.saude360.backendsaude360.enums.PaymentMethod;
import com.saude360.backendsaude360.enums.PaymentStatus;
import com.saude360.backendsaude360.enums.TransactionType;
import com.saude360.backendsaude360.repositories.TransactionRepository;
import com.saude360.backendsaude360.repositories.users.ProfessionalRepository;
import com.saude360.backendsaude360.exceptions.DatabaseException;
import com.saude360.backendsaude360.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionDto transactionDto;
    private Professional professional;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        transactionDto = new TransactionDto(
                "Test Transaction",
                BigDecimal.valueOf(1000),
                TransactionType.INCOME,
                LocalDate.now(),
                PaymentMethod.PIX,
                PaymentStatus.PENDING
        );

        professional = new Professional();
        professional.setCpf("12345678901");
    }

    @Test
    void testCreateTransaction() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("12345678901");
        SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null));

        when(professionalRepository.findByCpf(any(String.class))).thenReturn(professional);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction(transactionDto, professional));

        Transaction transaction = transactionService.create(transactionDto);

        assertNotNull(transaction);
        assertEquals(transactionDto.name(), transaction.getName());
        assertEquals(transactionDto.value(), transaction.getValue());
        assertEquals(transactionDto.transactionType(), transaction.getTransactionType());
        assertEquals(transactionDto.paymentMethod(), transaction.getPaymentMethod());
        assertEquals(transactionDto.paymentStatus(), transaction.getPaymentStatus());
    }

    @Test
    void testFindTransactionById() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction(transactionDto, professional);
        transaction.setId(transactionId);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        Optional<Transaction> foundTransaction = transactionService.findById(transactionId);

        assertTrue(foundTransaction.isPresent());
        assertEquals(transactionId, foundTransaction.get().getId());
    }

    @Test
    void testUpdateTransaction() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction(transactionDto, professional);
        transaction.setId(transactionId);

        TransactionDto updatedTransactionDto = new TransactionDto(
                "Updated Transaction",
                BigDecimal.valueOf(2000),
                TransactionType.EXPENSE,
                LocalDate.now(),
                PaymentMethod.CREDIT_CARD,
                PaymentStatus.CONCLUDED
        );

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Optional<Transaction> updatedTransaction = transactionService.update(transactionId, updatedTransactionDto);

        assertTrue(updatedTransaction.isPresent());
        assertEquals("Updated Transaction", updatedTransaction.get().getName());
        assertEquals(BigDecimal.valueOf(2000), updatedTransaction.get().getValue());
        assertEquals(TransactionType.EXPENSE, updatedTransaction.get().getTransactionType());
        assertEquals(PaymentMethod.CREDIT_CARD, updatedTransaction.get().getPaymentMethod());
        assertEquals(PaymentStatus.CONCLUDED, updatedTransaction.get().getPaymentStatus());
    }

    @Test
    void testDeleteTransaction() {
        Long transactionId = 1L;

        doNothing().when(transactionRepository).deleteById(transactionId);

        transactionService.delete(transactionId);

        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    void testDeleteTransaction_ThrowsObjectNotFoundException() {
        Long transactionId = 1L;

        doThrow(EmptyResultDataAccessException.class).when(transactionRepository).deleteById(transactionId);

        assertThrows(ObjectNotFoundException.class, () -> transactionService.delete(transactionId));
    }

    @Test
    void testDeleteTransaction_ThrowsDatabaseException() {
        Long transactionId = 1L;

        doThrow(DataIntegrityViolationException.class).when(transactionRepository).deleteById(transactionId);

        assertThrows(DatabaseException.class, () -> transactionService.delete(transactionId));
    }
}
