package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.*;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.mapper.ChatMapper;
import com.ms.mal_back.repository.AdvertisementRepository;
import com.ms.mal_back.repository.ChatRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.AdvertisementService;
import com.ms.mal_back.service.ChatService;
import com.ms.mal_back.service.DeliveryService;
import com.ms.mal_back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final ChatService chatService;
    private final DeliveryService deliveryService;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository adRepository;
    private final ChatMapper chatMapper;
    private final JwtService jwtService;

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserOverview>> getAllUsers(@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/ads")
    public ResponseEntity<List<AdvertisementResponse>> getAllAds(@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return ResponseEntity.ok(advertisementService.getAllAdvertisements());
    }

    @GetMapping("/user/{userId}/ads")
    public ResponseEntity<List<AdvertisementResponse>> getUserAds(@PathVariable Long userId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return ResponseEntity.ok(advertisementService.getAdvertisementsByUser(userId));
    }

    @GetMapping("/user/{userId}/ad/{adId}")
    public ResponseEntity<AdvertisementSingularResponse> getAdOfUser(
            @PathVariable Long userId,
            @PathVariable Long adId,
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        return ResponseEntity.ok(advertisementService.getAdvertisementById(adId, userId));
    }

    @DeleteMapping("/ad/{adId}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long adId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        Advertisement ad = advertisementService.getAdEntity(adId);
        advertisementService.deleteAdById(adId, ad.getSeller().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/chats")
    public ResponseEntity<List<ChatResponse>> getUserChats(@PathVariable Long userId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return ResponseEntity.ok(chatService.getChatsForUser(userId));
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<ChatSingularAdminResponse> getChat(@PathVariable Long chatId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        Chat chat = chatService.getChatEntity(chatId);
        return ResponseEntity.ok(chatMapper.toAdminResponse(chat));
    }

    @DeleteMapping("/chat/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long chatId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        chatRepository.deleteById(chatId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/user/{userId}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable Long userId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        User user = userService.getUserEntity(userId);
        user.setEnabled(false);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{userId}/enable")
    public ResponseEntity<Void> enableUser(@PathVariable Long userId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        User user = userService.getUserEntity(userId);
        user.setEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        userRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}/deliveries")
    public ResponseEntity<Void> deleteUserDeliveries(@PathVariable Long userId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        deliveryService.deleteAllByUserId(userId);
        return ResponseEntity.ok().build();
    }
}
