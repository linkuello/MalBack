package com.ms.mal_back.controller;

import com.ms.mal_back.dto.auth.AdminLoginRequest;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRole().getName();
        if (!role.equals("ADMIN") && !role.equals("OPERATOR")) {
            throw new RuntimeException("Access denied");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(token);
    }
}
