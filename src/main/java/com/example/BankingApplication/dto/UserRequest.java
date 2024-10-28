package com.example.BankingApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @Schema(description = "First Name", example = "Tony")
    private String firstName;
        @Schema(description = "last Name", example = "Stark")
    private String lastName;
        @Schema(description = "Other Name", example="RDJ")
    private String otherName;
        @Schema(description = "gender", example="Male")
    private String gender;
        @Schema(description = "Address", example="NewYork")
    private String address;
        @Schema(description = "State of Origin", example="NewYork")
    private String stateOfOrigin;
        @Schema(description = "Email", example="rdjtonystark@gmail.com")
    private String email;
        @Schema(description = "Phone Number", example="1234567890")
    private String phoneNumber;
        @Schema(description = "Alternative Phone Number", example="1234567890")
    private String  alternativePhoneNumber;

private String password;

}
