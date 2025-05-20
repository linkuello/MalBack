package com.ms.mal_back.mapper.impl;

import com.ms.mal_back.config.UrlBuilder;
import com.ms.mal_back.dto.ChatResponse;
import com.ms.mal_back.dto.ChatSingularAdminResponse;
import com.ms.mal_back.dto.ChatSingularResponse;
import com.ms.mal_back.dto.MessageResponse;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.mapper.ChatMapper;
import com.ms.mal_back.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatMapperImpl implements ChatMapper {

    private final MessageService messageService;
    private final UrlBuilder urlBuilder;

    @Override
    public Chat toEntity(Advertisement ad, User seller, User customer, LocalDateTime now) {
        Chat chat = new Chat();
        chat.setAdvertisement(ad);
        chat.setSeller(seller);
        chat.setCustomer(customer);
        chat.setLastMessageTime(now);
        return chat;
    }

    @Override
    public ChatResponse toChatResponse(Chat chat) {
        Advertisement ad = chat.getAdvertisement();
        String firstPhoto = ad.getPhotos().isEmpty() ? null :
                urlBuilder.buildFullPhotoUrl(ad.getPhotos().get(0).getId());

        return new ChatResponse(
                chat.getId(),
                firstPhoto,
                ad.getAnimal(),
                ad.getBreed(),
                chat.getLastMessageTime()
        );
    }

    @Override
    public List<ChatResponse> toChatResponseList(List<Chat> chats) {
        return chats.stream()
                .map(this::toChatResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ChatSingularResponse toSingularResponse(Chat chat, Long currentUserId) {
        Advertisement ad = chat.getAdvertisement();
        String firstPhoto = ad.getPhotos().isEmpty() ? null :
                urlBuilder.buildFullPhotoUrl(ad.getPhotos().get(0).getId());

        User other = chat.getSeller().getId().equals(currentUserId)
                ? chat.getCustomer()
                : chat.getSeller();

        ChatSingularResponse.UserPreview otherUser = new ChatSingularResponse.UserPreview(
                other.getId(),
                other.getUsername(),
                other.getPhoto() != null ? urlBuilder.buildFullPhotoUrl(other.getPhoto().getId()) : null
        );

        return new ChatSingularResponse(
                chat.getId(),
                ad.getId(),
                ad.getAnimal(),
                ad.getBreed(),
                ad.getPrice(),
                firstPhoto,
                otherUser,
                messageService.getAllMessagesDto(chat, currentUserId)
        );
    }
    @Override
    public ChatSingularAdminResponse toAdminResponse(Chat chat) {
        Advertisement ad = chat.getAdvertisement();
        String firstPhoto = ad.getPhotos().isEmpty() ? null :
                urlBuilder.buildFullPhotoUrl(ad.getPhotos().get(0).getId());

        ChatSingularAdminResponse.UserPreview seller = new ChatSingularAdminResponse.UserPreview(
                chat.getSeller().getId(),
                chat.getSeller().getUsername(),
                chat.getSeller().getPhoto() != null
                        ? urlBuilder.buildFullPhotoUrl(chat.getSeller().getPhoto().getId())
                        : null
        );

        ChatSingularAdminResponse.UserPreview customer = new ChatSingularAdminResponse.UserPreview(
                chat.getCustomer().getId(),
                chat.getCustomer().getUsername(),
                chat.getCustomer().getPhoto() != null
                        ? urlBuilder.buildFullPhotoUrl(chat.getCustomer().getPhoto().getId())
                        : null
        );

        List<MessageResponse> messages = messageService.getAllMessagesDto(chat, null); // null: admin = no filter

        return new ChatSingularAdminResponse(
                chat.getId(),
                ad.getId(),
                ad.getAnimal(),
                ad.getBreed(),
                ad.getPrice(),
                firstPhoto,
                seller,
                customer,
                messages
        );
    }

}


