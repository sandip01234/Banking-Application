package com.example.BankingApplication.service;

import com.example.BankingApplication.dto.Transactioninfo;
import com.example.BankingApplication.entity.Transaction;
import com.example.BankingApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Component
public class TransactionServiceImp implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transactioninfo transactioninfo) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactioninfo.getTransactionType())
                .accountNumber(transactioninfo.getAccountNumber())
                .amount(transactioninfo.getAmount())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now().plusDays(1))
                .build();
        try {
            transactionRepository.save(transaction);
            System.out.println("Transaction saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to save transaction: " + e.getMessage());
        }
    }




}