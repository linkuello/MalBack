package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.MessageRequest;
import com.ms.mal_back.dto.MessageResponse;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.service.ChatService;
import com.ms.mal_back.service.MessageService;
import com.ms.mal_back.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final JwtService jwtService;
    private final UserService userService;
    private final ChatService chatService;
    private final MessageService messageService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{chatId}")
    public ResponseEntity<Void> sendMessage(
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId,
            @RequestBody MessageRequest request
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        User sender = userService.getUserEntity(userId);
        Chat chat = chatService.getChatEntity(chatId);

        messageService.createMessageFromRequest(chat, sender, request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{chatId}/unread")
    public ResponseEntity<List<MessageResponse>> getUnreadMessages(
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        Chat chat = chatService.getChatEntity(chatId);

        List<MessageResponse> unread = messageService.getUnreadMessagesDto(chat, userId);
        messageService.markMessagesAsRead(
                chat.getMessages().stream()
                        .filter(m -> !m.getSender().getId().equals(userId) && !m.isRead())
                        .toList()
        );

        return ResponseEntity.ok(unread);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{chatId}")
    public ResponseEntity<List<MessageResponse>> getAllMessages(
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        Chat chat = chatService.getChatEntity(chatId);

        return ResponseEntity.ok(messageService.getAllMessagesDto(chat, userId));
    }
}
