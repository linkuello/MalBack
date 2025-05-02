package com.ms.mal_back.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("JWT filter triggered");
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // skip if no auth
            return;
        }

        final String token = authHeader.substring(7);

        try {
            Long userId = jwtService.extractUserId(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findById(userId).orElse(null);

                if (user != null && jwtService.isTokenValid(token)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            filterChain.doFilter(request, response); // ✅ success

        } catch (ExpiredJwtException e) {
            sendJsonError(response, HttpStatus.UNAUTHORIZED.value(), "Token expired");
        } catch (JwtException e) {
            sendJsonError(response, HttpStatus.UNAUTHORIZED.value(), "Invalid token");
        }
    }

    private void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", status);
        errorBody.put("message", message);
        errorBody.put("timestamp", System.currentTimeMillis());

        objectMapper.writeValue(response.getWriter(), errorBody);
    }
}