package com.ms.mal_back.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    public void sendPasswordResetLink(String email, String token);
}
