package com.example.BankingApplication.service;

import com.example.BankingApplication.config.JwttokenProvider;
import com.example.BankingApplication.dto.*;
import com.example.BankingApplication.entity.Role;
import com.example.BankingApplication.entity.user;
import com.example.BankingApplication.repository.UserRepository;
import com.example.BankingApplication.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.emitter.Emitable;

import java.math.BigDecimal;
@AllArgsConstructor
@Service
public class UserServiceimp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwttokenProvider jwttokenProvider;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        // Check if an account with the given email already exists
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MASSAGE)
                    .accountinfo(null)
                    .build();
        }

        // Create a new user entity
        user newUser = user.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNubmer())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .role(Role.ADMIN)
                .build();

        // Save the new user to the database(user) usertocredit).setAccountBalance(((user) usertocredit).getAccountBalance().add(userRequest.getAmount()));
        user savedUser = userRepository.save(newUser);

        // Create a transaction info object for the initial account creation
        Transactioninfo transactioninfo = Transactioninfo.builder()
                .transactionType("CREDIT")
                .accountNumber(savedUser.getAccountNumber())
                .amount(savedUser.getAmount())
                .status("Success")
                .build();

        // Save the transaction to the database
        transactionService.saveTransaction(transactioninfo);

        // Prepare email details for account creation notification
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION!")
                .messageBody("Congratulations! Your Account Has Been Successfully Created.\nYour Account Details:\n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\n" +
                        "Account Number: " + savedUser.getAccountNumber())
                .build();

        // Send the account creation email
        emailService.sendEmailAlert(emailDetails);

        // Return a successful response with account details
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MASSAGE)
                .accountinfo(Accountinfo.builder()
                        .accountBalance(String.valueOf(savedUser.getAccountBalance()))
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();
    }
     public BankResponse login(loginDto logindto){
         Authentication authentication= null;
         authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(logindto.getEmail(),logindto.getPassword())
         );
         EmailDetails loginAlert = EmailDetails.builder()
                 .recipient(logindto.getEmail())
                 .subject("LOGIN ALERT!")
                 .messageBody("You have successfully logged in to your account.")
                 .build();
            emailService.sendEmailAlert(loginAlert);
            return BankResponse.builder()
                    .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
                   // .responseMessage(AccountUtils.LOGIN_SUCCESS_MASSAGE)
                    .responseMessage(jwttokenProvider.generateToken(authentication))
                    .build();
     }








    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // Check if the account exists by account number
        boolean isaccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isaccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MASSAGE)
                    .accountinfo(null)
                    .build();
        }

        // Retrieve the user by account number
        user user = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountinfo(Accountinfo.builder()
                        .accountBalance(String.valueOf(user.getAccountBalance()))
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        // Check if the account exists by account number
        boolean isaccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isaccountExists) {
            return AccountUtils.ACCOUNT_NOT_EXISTS_MASSAGE;
        }

        // Retrieve the user by account number
        user user = userRepository.findByAccountNumber(request.getAccountNumber());
        return user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
    }

    public BankResponse creditAccount(CreditDebitRequest request) {
        // Check if the account exists by account number
        boolean isaccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isaccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MASSAGE)
                    .accountinfo(null)
                    .build();
        }

        // Retrieve the user by account number
        user usertocredit = userRepository.findByAccountNumber(request.getAccountNumber());

        // Add the amount to the account balance
        BigDecimal newBalance = usertocredit.getAccountBalance().add(request.getAmount());

        // Save the new balance
        usertocredit.setAccountBalance(newBalance);
        userRepository.save(usertocredit);

        // Return a successful response with updated account details
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountinfo(Accountinfo.builder()
                        .accountBalance(String.valueOf(newBalance))
                        .accountNumber(usertocredit.getAccountNumber())
                        .accountName(usertocredit.getFirstName() + " " + usertocredit.getLastName() + " " + usertocredit.getOtherName())
                        .build())
                .build();
    }

    public BankResponse debitAccount(CreditDebitRequest request) {
        // Check if the account exists by account number
        boolean isaccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isaccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MASSAGE)
                    .accountinfo(null)
                    .build();
        }

        // Retrieve the user by account number
        user usertodebit = userRepository.findByAccountNumber(request.getAccountNumber());

        // Check if the account balance is greater than the amount to be debited
        if (usertodebit.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MASSAGE)
                    .accountinfo(null)
                    .build();
        }

        // Subtract the amount from the account balance
        BigDecimal newBalance = usertodebit.getAccountBalance().subtract(request.getAmount());

        // Save the new balance
        usertodebit.setAccountBalance(newBalance);
        userRepository.save(usertodebit);

        // Create a transaction info object for the debit transaction
        Transactioninfo transactionInfo = Transactioninfo.builder()
                .transactionType("DEBIT")
                .accountNumber(usertodebit.getAccountNumber())
                .amount(request.getAmount())
                .status("Success")
                .build();

        // Save the transaction to the database
        transactionService.saveTransaction(transactionInfo);

        // Return a successful response with updated account details
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountinfo(Accountinfo.builder()
                        .accountBalance(String.valueOf(newBalance))
                        .accountNumber(usertodebit.getAccountNumber())
                        .accountName(usertodebit.getFirstName() + " " + usertodebit.getLastName() + " " + usertodebit.getOtherName())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public BankResponse transfer(Transfer request) {
        // Check if the source and destination accounts exist
        boolean isSourceAccountExists = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isSourceAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_MASSAGE)
                    .accountinfo(null)
                    .build();
        }
        if (!isDestinationAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.DESTINATION_ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.DESTINATION_ACCOUNT_NOT_EXISTS_MASSAGE)
                    .accountinfo(null)
                    .build();
        }

        // Retrieve the source and destination users by account number
        user sourceUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        user destinationUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());

        // Check if the source account balance is greater than the amount to be transferred
        if (sourceUser.getAccountBalance().compareTo(BigDecimal.valueOf(request.getAmount())) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MASSAGE)
                    .accountinfo(null)
                    .build();
        }

        // Subtract the amount from the source account balance
        BigDecimal newSourceBalance = sourceUser.getAccountBalance().subtract(BigDecimal.valueOf(request.getAmount()));

        // Add the amount to the destination account balance
        BigDecimal newDestinationBalance = destinationUser.getAccountBalance().add(BigDecimal.valueOf(request.getAmount()));

        // Save the new balances
        sourceUser.setAccountBalance(newSourceBalance);
        destinationUser.setAccountBalance(newDestinationBalance);
        userRepository.save(sourceUser);
        userRepository.save(destinationUser);

        // Prepare email alerts for both source and destination users
        String sourceUserName = sourceUser.getFirstName() + " " + sourceUser.getLastName() + " " + sourceUser.getOtherName();
        String destinationUserName = destinationUser.getFirstName() + " " + destinationUser.getLastName() + " " + destinationUser.getOtherName();

        EmailDetails debitAlert = EmailDetails.builder()
                .recipient(sourceUser.getEmail())
                .subject("DEBIT ALERT!")
                .messageBody("You have successfully debited your account with the sum of " + request.getAmount() + " Naira.\n" +
                        "by " + destinationUserName + "\n Your new account balance is " + newSourceBalance + " Naira.")
                .build();

        EmailDetails creditAlert = EmailDetails.builder()
                .recipient(destinationUser.getEmail())
                .subject("CREDIT ALERT!")
                .messageBody("You have successfully credited your account with the sum of " + request.getAmount() +
                        " to " + sourceUserName + "\n Your new account balance is " + newDestinationBalance + " Naira.")
                .build();

        // Send email alerts
        emailService.sendEmailAlert(debitAlert);
        emailService.sendEmailAlert(creditAlert);



         //Save the transaction to the database
        Transactioninfo transactionInfo = Transactioninfo.builder()
                .transactionType("TRANSFER")
                .accountNumber(sourceUser.getAccountNumber())
                .amount(BigDecimal.valueOf(request.getAmount()))
                .status("Success")
                .build();
        transactionService.saveTransaction(transactionInfo);

        // Return a successful response with updated source account details
        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MASSAGE)
                .accountinfo(Accountinfo.builder()
                        .accountBalance(String.valueOf(newSourceBalance))
                        .accountNumber(sourceUser.getAccountNumber())
                        .accountName(sourceUser.getFirstName() + " " + sourceUser.getLastName() + " " + sourceUser.getOtherName())
                        .build())
                .build();
    }




}