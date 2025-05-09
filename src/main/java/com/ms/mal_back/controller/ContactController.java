package com.ms.mal_back.controller;

import com.ms.mal_back.dto.ContactUsMessage;
import com.ms.mal_back.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class ContactController {

    private final EmailService emailService;

    @PostMapping("/contact")
    public ResponseEntity<Void> submitMessage(@RequestBody ContactUsMessage message) {
        if (message.getEmail() == null || message.getContent() == null || message.getTopic() == null) {
            return ResponseEntity.badRequest().build();
        }

        String subject = "Contact Us — " + message.getTopic();
        String body = "From: " + message.getEmail() + "\n\n" + message.getContent();

        emailService.sendEmail("admin@yourdomain.com", subject, body);

        return ResponseEntity.ok().build();
    }
}
