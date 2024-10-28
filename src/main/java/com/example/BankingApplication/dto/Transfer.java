package com.example.BankingApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transfer {
    @Schema(description = "Source Account Number", example = "1234567890")
private String sourceAccountNumber;
    @Schema(description = "Destination Account Number", example = "1234567890")
private String destinationAccountNumber;
    @Schema(description = "Amount", example = "1000")
private Double amount;
}
