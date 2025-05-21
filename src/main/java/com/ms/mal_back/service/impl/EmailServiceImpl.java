package com.ms.mal_back.service.impl;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetLink(String email, String token) {
        String link = "https://malsat.onrender.com/reset-password?token=" + token;
        String subject = "MalSat.kg - Password Reset";
        String body = "To reset your password, click the link below:\n\n" +
                link + "\n\nThis link will expire in 15 minutes.";
        sendEmail(email, subject, body);
    }
}
