package com.ms.mal_back.service.impl;

import com.ms.mal_back.dto.*;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.exception.ForbiddenAccessException;
import com.ms.mal_back.mapper.ChatMapper;
import com.ms.mal_back.repository.AdvertisementRepository;
import com.ms.mal_back.repository.ChatRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

    @Override
    public ChatCreatedResponse createChat(Long customerId, ChatRequest request) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Advertisement ad = advertisementRepository.findById(request.getAdvertisementId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advertisement not found"));

        User seller = ad.getSeller();

        if (seller.getId().equals(customerId)) {
            throw new IllegalArgumentException("Cannot create chat with yourself");
        }

        Optional<Chat> existingChat = chatRepository
                .findByAdvertisementAndSellerAndCustomer(ad, seller, customer);
        if (existingChat.isPresent()) {
            return new ChatCreatedResponse(existingChat.get().getId());
        }

        Chat chat = chatMapper.toEntity(ad, seller, customer, LocalDateTime.now());
        chatRepository.save(chat);

        return new ChatCreatedResponse(chat.getId());
    }

    @Override
    public List<ChatResponse> getChatsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Chat> chats = chatRepository.findAllByUserIdWithAd(userId);
        chats.sort(Comparator.comparing(Chat::getLastMessageTime).reversed());
        return chatMapper.toChatResponseList(chats);
    }

    @Override
    public ChatSingularAdminResponse getChatAsAdmin(Long chatId){
        Chat chat = chatRepository.findByIdWithMessages(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        return chatMapper.toAdminResponse(chat);
    }

    @Override
    public ChatSingularResponse getChatById(Long chatId, Long currentUserId) {
        Chat chat = chatRepository.findByIdWithMessages(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));

        if (!chat.getSeller().getId().equals(currentUserId) &&
                !chat.getCustomer().getId().equals(currentUserId)) {
            throw new ForbiddenAccessException("You are not part of this chat.");
        }


        return chatMapper.toSingularResponse(chat, currentUserId);
    }

    @Override
    public Chat getChatEntity(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
    }
}

