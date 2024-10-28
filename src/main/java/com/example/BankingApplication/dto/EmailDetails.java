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
public class EmailDetails {
    @Schema(description = "Recipient Email Address", example = "abc@gmail.com")
private String recipient;
    @Schema(description = "Message Body", example = "Hello, This is a test email")
private String messageBody;
    @Schema(description = "Subject", example = "Test Email")
private  String subject;
    @Schema(description = "Attachment", example = "C:/Users/abc/Downloads/test.pdf")
private String attachment;

}
