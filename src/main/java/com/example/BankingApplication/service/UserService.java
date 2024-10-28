package com.example.BankingApplication.service;

import com.example.BankingApplication.dto.*;


public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
  BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(Transfer request);

    BankResponse login(loginDto request);
}
