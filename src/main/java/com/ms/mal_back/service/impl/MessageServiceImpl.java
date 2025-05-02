package com.ms.mal_back.service.impl;

import com.ms.mal_back.dto.MessageRequest;
import com.ms.mal_back.dto.MessageResponse;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.Message;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.mapper.MessageMapper;
import com.ms.mal_back.repository.ChatRepository;
import com.ms.mal_back.repository.MessageRepository;
import com.ms.mal_back.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ChatRepository chatRepository;

    @Override
    public Message createMessageFromRequest(Chat chat, User sender, MessageRequest request) {
        Message message = messageMapper.toEntity(request, chat, sender);
        Message saved = messageRepository.save(message);

        chat.setLastMessageTime(saved.getSentAt());
        chatRepository.save(chat);
        List<Message> allMessages = chat.getMessages();
        if (allMessages.size() > 500) {
            List<Message> toDelete = allMessages.subList(0, allMessages.size() - 500);
            messageRepository.deleteAll(toDelete);
            chat.getMessages().removeAll(toDelete);
        }
        return saved;
    }


    @Override
    public List<Message> getUnreadMessagesForUser(Chat chat, Long userId) {
        return messageRepository.findByChatAndSenderIdNotAndIsReadFalse(chat, userId);
    }

    @Override
    public List<MessageResponse> getUnreadMessagesDto(Chat chat, Long userId) {
        List<Message> messages = getUnreadMessagesForUser(chat, userId);
        markMessagesAsRead(messages);
        return messageMapper.toDtos(messages, userId);
    }

    @Override
    public void markMessagesAsRead(List<Message> messages) {
        messages.forEach(m -> m.setRead(true));
        messageRepository.saveAll(messages);
    }

    @Override
    public List<MessageResponse> getAllMessagesDto(Chat chat, Long userId) {
        return messageMapper.toDtos(chat.getMessages(), userId);
    }
}

