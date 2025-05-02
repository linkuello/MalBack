package com.ms.mal_back.controller;

import com.ms.mal_back.dto.ChatCreatedResponse;
import com.ms.mal_back.dto.ChatRequest;
import com.ms.mal_back.dto.ChatResponse;
import com.ms.mal_back.dto.ChatSingularResponse;
import com.ms.mal_back.service.ChatService;
import com.ms.mal_back.config.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ChatController {

    private final ChatService chatService;
    private final JwtService jwtService;

    @Operation(summary = "Create a new chat for an advertisement")
    @PostMapping("/create")
    public ResponseEntity<ChatCreatedResponse> createChat(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,
            @RequestBody ChatRequest request
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);

        log.info("Creating chat for user {} for advertisement {}", userId, request.getAdvertisementId());
        ChatCreatedResponse response = chatService.createChat(userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all chats for the authenticated user")
    @GetMapping("/my-chats")
    public ResponseEntity<List<ChatResponse>> getChatsForUser(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);

        log.info("Fetching all chats for user {}", userId);
        return ResponseEntity.ok(chatService.getChatsForUser(userId));
    }

    @Operation(summary = "Get a single chat by ID for the authenticated user")
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatSingularResponse> getChatById(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);

        log.info("Fetching chat {} for user {}", chatId, userId);
        return ResponseEntity.ok(chatService.getChatById(chatId, userId));
    }
}
