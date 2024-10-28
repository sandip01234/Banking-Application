package com.example.BankingApplication.service;

import com.example.BankingApplication.dto.EmailDetails;

public interface EmailService {
boolean sendEmailAlert(EmailDetails emailDetails);
void sendEmailWithAttachement(EmailDetails emailDetails);
}
