package com.ms.mal_back.init;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "ertai-loh";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}

