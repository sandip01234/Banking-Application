package com.example.BankingApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Accountinfo {
    @Schema(description = "Account Name", example = "Sandip Kumar Shah")
    private String accountName;
    @Schema(description = "Account Balance", example = "100000")
    private String accountBalance;
    @Schema(description = "Account Number", example = "1234567890")
    private String accountNumber;
}
