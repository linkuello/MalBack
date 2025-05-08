package com.ms.mal_back.controller;

import com.ms.mal_back.dto.ChatCreatedResponse;
import com.ms.mal_back.dto.ChatRequest;
import com.ms.mal_back.dto.ChatResponse;
import com.ms.mal_back.dto.ChatSingularResponse;
import com.ms.mal_back.service.ChatService;
import com.ms.mal_back.config.JwtService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ChatController {

    private final ChatService chatService;
    private final JwtService jwtService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<ChatCreatedResponse> createChat(
            @RequestHeader("Authorization") String token,
            @RequestBody ChatRequest request
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(chatService.createChat(userId, request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-chats")
    public ResponseEntity<List<ChatResponse>> getChatsForUser(
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(chatService.getChatsForUser(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatSingularResponse> getChatById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(chatService.getChatById(chatId, userId));
    }
}
