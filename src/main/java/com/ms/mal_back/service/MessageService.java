package com.ms.mal_back.service;

import com.ms.mal_back.dto.MessageRequest;
import com.ms.mal_back.dto.MessageResponse;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.Message;
import com.ms.mal_back.entity.User;

import java.util.List;

public interface MessageService {
    Message createMessageFromRequest(Chat chat, User sender, MessageRequest request);
    List<Message> getUnreadMessagesForUser(Chat chat, Long userId);
    List<MessageResponse> getUnreadMessagesDto(Chat chat, Long userId);
    void markMessagesAsRead(List<Message> messages);
    List<MessageResponse> getAllMessagesDto(Chat chat, Long currentUserId);
}
