package com.example.BankingApplication.repository;

import com.example.BankingApplication.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountNumber(String accountNumber);

   // List<Transaction> findByAccountNumberAndDateBetween(String accountNumber, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByAccountNumberAndCreatedAtBetween(String accountNumber, LocalDateTime startDateTime, LocalDateTime endDateTime);
}