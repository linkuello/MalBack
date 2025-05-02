package com.ms.mal_back.mapper;

import com.ms.mal_back.dto.MessageRequest;
import com.ms.mal_back.dto.MessageResponse;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.Message;
import com.ms.mal_back.entity.User;

import java.util.List;

public interface MessageMapper {
    Message toEntity(MessageRequest request, Chat chat, User sender);
    MessageResponse toDto(Message message, Long currentUserId);
    List<MessageResponse> toDtos(List<Message> messages, Long currentUserId);
}
