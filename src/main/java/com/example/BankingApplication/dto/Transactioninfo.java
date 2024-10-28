package com.example.BankingApplication.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class Transactioninfo {

    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;

}
