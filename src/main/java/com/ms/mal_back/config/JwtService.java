package com.ms.mal_back.config;

import com.ms.mal_back.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;  // No longer static

    @Value("${application.security.jwt.expiration}")
    private Long jwtLifeTime;

    public String generateToken(UserDetails userDetails) {
        User user = (User) userDetails;
        Map<String, Object> claims = new HashMap<>();
        String role = userDetails.getAuthorities().toString();
        claims.put("role", role);
        claims.put("userId", user.getId());

        Date issuedDate = new Date();
        Date expireDate = new Date(issuedDate.getTime() + jwtLifeTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expireDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String getUserEmail(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }
    public String getRole(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }
    public void validateToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("Token expired");
            }
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token", e);
        }
    }
    public Long extractUserId(String token) { return getAllClaimsFromToken(token).get("userId", Long.class); }
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch ( JwtException e) {
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateEmailConfirmationToken(String email) {
        Date issuedDate = new Date();
        Date expireDate = new Date(issuedDate.getTime() + 1000 * 60 * 60 * 24); // 24 часа

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedDate)
                .setExpiration(expireDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractEmailFromConfirmationToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }
}

