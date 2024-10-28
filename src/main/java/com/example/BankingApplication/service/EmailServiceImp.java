package com.example.BankingApplication.service;

import com.example.BankingApplication.dto.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class EmailServiceImp implements EmailService {
@Autowired
    private JavaMailSender javaMailSender;
@Value("${spring.mail.username}")
private String senderEmail;


    @Override
    public boolean sendEmailAlert(EmailDetails emailDetails) {
    try{
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(senderEmail);
        mailMessage.setTo(emailDetails.getRecipient());
        mailMessage.setText(emailDetails.getMessageBody());
        mailMessage.setSubject(emailDetails.getSubject());
        javaMailSender.send(mailMessage);
        System.out.println("Mail sent succesfully!!");
    } catch (MailException e) {
        throw new RuntimeException(e);
    }
        return false;
    }

    //for sending email with bankstatement as attachment
    @Override
    public void sendEmailWithAttachement(EmailDetails emailDetails) {
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMessageBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            FileSystemResource file= new FileSystemResource(emailDetails.getAttachment());
        mimeMessageHelper.addAttachment(file.getFilename(),file);
        javaMailSender.send(mimeMessage);
        log.info(file.getFilename()+ " has been sent to user with email"+emailDetails.getRecipient());


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
