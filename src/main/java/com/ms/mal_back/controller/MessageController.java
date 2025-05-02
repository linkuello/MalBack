package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.MessageRequest;
import com.ms.mal_back.dto.MessageResponse;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.service.ChatService;
import com.ms.mal_back.service.MessageService;
import com.ms.mal_back.service.UserService;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final JwtService jwtService;
    private final UserService userService;
    private final ChatService chatService;
    private final MessageService messageService;

    @Operation(summary = "Send a message in a chat")
    @PostMapping("/{chatId}")
    public ResponseEntity<Void> sendMessage(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId,
            @RequestBody MessageRequest request
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        User sender = userService.getUserEntity(userId);
        Chat chat = chatService.getChatEntity(chatId);

        log.info("User {} is sending message to chat {}", userId, chatId);
        messageService.createMessageFromRequest(chat, sender, request);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get unread messages in a chat for current user")
    @GetMapping("/{chatId}/unread")
    public ResponseEntity<List<MessageResponse>> getUnreadMessages(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        Chat chat = chatService.getChatEntity(chatId);

        log.info("Fetching unread messages for user {} in chat {}", userId, chatId);
        List<MessageResponse> unread = messageService.getUnreadMessagesDto(chat, userId);

        messageService.markMessagesAsRead(
                chat.getMessages().stream()
                        .filter(m -> !m.getSender().getId().equals(userId) && !m.isRead())
                        .toList()
        );

        return ResponseEntity.ok(unread);
    }

    @Operation(summary = "Get all messages in a chat for current user")
    @GetMapping("/{chatId}")
    public ResponseEntity<List<MessageResponse>> getAllMessages(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        Chat chat = chatService.getChatEntity(chatId);

        log.info("Fetching all messages for user {} in chat {}", userId, chatId);
        return ResponseEntity.ok(messageService.getAllMessagesDto(chat, userId));
    }
}
