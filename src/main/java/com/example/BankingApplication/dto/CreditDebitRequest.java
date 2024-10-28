package com.example.BankingApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDebitRequest {
    @Schema(description = "Account Number", example = "1234567890")
    private String accountNumber;
    @Schema(description = "Amount", example = "1000")
    private BigDecimal amount;


}
