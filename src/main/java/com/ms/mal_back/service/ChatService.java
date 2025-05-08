package com.ms.mal_back.service;

import com.ms.mal_back.dto.*;
import com.ms.mal_back.entity.Chat;

import java.util.List;

public interface ChatService {
    ChatCreatedResponse createChat(Long customerId, ChatRequest request);
    List<ChatResponse> getChatsForUser(Long userId);
    ChatSingularResponse getChatById(Long chatId, Long currentUserId);
    Chat getChatEntity(Long chatId);
    ChatSingularAdminResponse getChatAsAdmin(Long chatId);
}
