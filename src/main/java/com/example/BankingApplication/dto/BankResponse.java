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
public class BankResponse {
    @Schema(description = "Response Code", example = "200")
    private String responseCode;
    @Schema(description = "Response Message", example = "Success")
    private String responseMessage;
    @Schema(description = "Account Information")
    private Accountinfo accountinfo;
}
