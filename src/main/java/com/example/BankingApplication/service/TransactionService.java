package com.example.BankingApplication.service;

import com.example.BankingApplication.dto.Transactioninfo;
import org.springframework.stereotype.Service;


public interface TransactionService {
    public void saveTransaction(Transactioninfo transactioninfo);
}
