package com.ms.mal_back.mapper.impl;

import com.ms.mal_back.dto.MessageRequest;
import com.ms.mal_back.dto.MessageResponse;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.Message;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.mapper.MessageMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public Message toEntity(MessageRequest request, Chat chat, User sender) {
        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setText(request.getText());
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);
        return message;
    }

    @Override
    public MessageResponse toDto(Message message, Long currentUserId) {
        boolean isMine = message.getSender().getId().equals(currentUserId);

        return new MessageResponse(
                message.getText(),
                message.getSentAt(),
                message.isRead(),
                isMine
        );
    }

    @Override
    public List<MessageResponse> toDtos(List<Message> messages, Long currentUserId) {
        return messages.stream()
                .map(m -> toDto(m, currentUserId))
                .collect(Collectors.toList());
    }
}

