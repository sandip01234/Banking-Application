package com.example.BankingApplication.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE ="001";
    public static final String ACCOUNT_EXISTS_MASSAGE ="This user already exits:";
    public static final String ACCOUNT_CREATION_SUCCESS ="002";
    public static final String ACCOUNT_CREATION_MASSAGE="The account has been created!";
    public static final String INSUFFICIENT_BALANCE_CODE ="005";
    public static final String INSUFFICIENT_BALANCE_MASSAGE = "Insufficient balance!";
    public static final String SOURCE_ACCOUNT_NOT_EXISTS_CODE ="006" ;
    public static final String SOURCE_ACCOUNT_NOT_EXISTS_MASSAGE = "The source account does not exist!";
    public static final String DESTINATION_ACCOUNT_NOT_EXISTS_CODE = "007";
    public static final String DESTINATION_ACCOUNT_NOT_EXISTS_MASSAGE = "The destination account does not exist!";
    public static final String TRANSFER_SUCCESS_CODE = "008";
    public static final String TRANSFER_SUCCESS_MASSAGE ="The transfer has been successful!";
    public static final String LOGIN_SUCCESS_CODE = "009";
    public static final String LOGIN_SUCCESS_MASSAGE = "Login successful!";
    ;
    ;


    public static String ACCOUNT_NOT_EXISTS_CODE="003";
    public static String ACCOUNT_NOT_EXISTS_MASSAGE="This account does not exist!";
    public static String ACCOUNT_FOUND_CODE="004";
    public static String ACCOUNT_FOUND_SUCCESS="The account has been found!";
    public static String generateAccountNubmer() {

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        //generation of a random number of 6 digit between min and max:
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        //convert currentYear and randNumber to Strings:
        String year = String.valueOf(currentYear);
        String randonNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();
       return accountNumber.append(year).append(randNumber).toString();
    }


}
