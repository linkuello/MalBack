package com.ms.mal_back.mapper;

import com.ms.mal_back.dto.ChatResponse;
import com.ms.mal_back.dto.ChatSingularAdminResponse;
import com.ms.mal_back.dto.ChatSingularResponse;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMapper {
    Chat toEntity( Advertisement ad, User seller, User customer, LocalDateTime now);
    ChatResponse toChatResponse(Chat chat);
    List<ChatResponse> toChatResponseList(List<Chat> chats);
    ChatSingularResponse toSingularResponse(Chat chat, Long currentUserId);
    public ChatSingularAdminResponse toAdminResponse(Chat chat);
}