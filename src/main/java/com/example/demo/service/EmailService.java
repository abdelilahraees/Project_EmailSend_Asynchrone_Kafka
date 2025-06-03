package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${emails.sender.email}")
    private String senderEmail;
    @Value("${emails.sender_name}")
    private String senderName;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Async
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderName + " <" + senderEmail + ">");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}