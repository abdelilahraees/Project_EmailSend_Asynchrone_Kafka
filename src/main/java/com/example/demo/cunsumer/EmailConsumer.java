package com.example.demo.cunsumer;

import com.example.demo.dto.EmailEvent;
import com.example.demo.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class EmailConsumer {
    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);
    private final EmailService emailService;
    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }
    @KafkaListener(topics = "${app.kafka.email-topic}", groupId = "${app.kafka.email-group}")
    public void consumeEmailEvent(String jsonEmailEvent) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            EmailEvent emailEvent = mapper.readValue(jsonEmailEvent, EmailEvent.class);
            emailService.sendSimpleEmail(
                    emailEvent.getTo(),
                    emailEvent.getSubject(),
                    emailEvent.getText()
            );
        } catch (Exception e) {
            log.error("Failed to process email event: {}", e.getMessage(), e);
        }
    }
}