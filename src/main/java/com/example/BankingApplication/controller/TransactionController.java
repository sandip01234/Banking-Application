package com.example.BankingApplication.controller;

import com.example.BankingApplication.entity.Transaction;
import com.example.BankingApplication.service.BankStatement;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
public class TransactionController {
private BankStatement bankStatement;
@GetMapping("/statement")
public List<Transaction> getStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) throws DocumentException, FileNotFoundException {
    return bankStatement.generateStatement(accountNumber, startDate, endDate);
}
}
