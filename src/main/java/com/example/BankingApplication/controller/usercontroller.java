package com.example.BankingApplication.controller;

import com.example.BankingApplication.dto.*;
import com.example.BankingApplication.entity.Transaction;
import com.example.BankingApplication.service.BankStatement;
import com.example.BankingApplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "User Account Management APIs")
public class usercontroller {
    @Autowired
    UserService userService;
@Operation(
        summary = "Create a new account",
        description = "Create a new account for the user"
)
@ApiResponse(responseCode = "200", description = "Account created successfully")
    @PostMapping("/user")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }
@Operation(
        summary = "Get account balance",
        description = "Get account balance for the user"
)

@ApiResponse(responseCode = "200", description = "Account balance fetched successfully")
    @GetMapping("/BalanceEnquiry")
    public BankResponse getAccountBalance(@RequestBody EnquiryRequest request) {
        return userService.balanceEnquiry(request);

    }
@Operation(
        summary = "Get account name",
        description = "Get account name for the user"
)
@ApiResponse(responseCode = "200", description = "Account name fetched successfully")

    @GetMapping("/nameEnquiry")
    public String getAccountName(@RequestBody EnquiryRequest request) {
        return userService.nameEnquiry(request);

    }
    @PostMapping("/creditAccount")
    @Operation(
            summary = "Credit account",
            description = "Credit account for the user"
    )
    @ApiResponse(responseCode = "200", description = "Account credited successfully")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);

    }
    @Operation(
            summary = "Debit account",
            description = "Debit account for the user"
    )
    @ApiResponse(responseCode = "200", description = "Account debited successfully")

    @PostMapping("/debitAccount")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);

    }

    @Operation(
            summary = "Login",
            description = "Login for the user"
    )
    @PostMapping("/login")
    public BankResponse login(@RequestBody loginDto request) {
        return userService.login(request);
    }

    @Operation(
            summary = "Transfer money",
            description = "Transfer money from one account to another")
    @ApiResponse(responseCode = "200", description = "Money transferred successfully")
    @PostMapping("/transfer")
    public BankResponse transfermoney(@RequestBody Transfer request) {
        return userService.transfer(request);
    }

}