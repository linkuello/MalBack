package com.ms.mal_back.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT has expired");
    }

    @ExceptionHandler(io.jsonwebtoken.SignatureException.class)
    public ResponseEntity<String> handleInvalidSignature(SignatureException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT signature");
    }

    @ExceptionHandler(io.jsonwebtoken.MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwt(MalformedJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Malformed JWT token");
    }

    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<String> handleJwtException(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT error: " + ex.getMessage());
    }
}
