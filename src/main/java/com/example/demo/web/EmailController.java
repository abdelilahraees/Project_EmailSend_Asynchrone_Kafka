package com.example.demo.web;

import com.example.demo.dto.EmailEvent;
import com.example.demo.dto.EmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<Object, String> kafkaTemplate;
    private final String emailTopic;

    public EmailController(KafkaTemplate<Object, String> kafkaTemplate, @Value("${app.kafka.email-topic}") String emailTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.emailTopic = emailTopic;
    }


    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) throws JsonProcessingException {
        // ... validation code ...

        EmailEvent emailEvent = new EmailEvent(request.getTo(), request.getSubject(), request.getText());
        String emailEventJson = objectMapper.writeValueAsString(emailEvent);
        kafkaTemplate.send(emailTopic, emailEventJson);

        return ResponseEntity.ok("Email request accepted and queued for processing!");
    }
}