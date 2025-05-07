package com.ms.mal_back.service.impl;

import com.ms.mal_back.dto.AdvertisementResponse;
import com.ms.mal_back.dto.ChatResponse;
import com.ms.mal_back.dto.admin.AdminUserDetailsResponse;
import com.ms.mal_back.dto.admin.UserResponse;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.repository.AdvertisementRepository;
import com.ms.mal_back.repository.ChatRepository;
import com.ms.mal_back.mapper.AdvertisementMapper;
import com.ms.mal_back.mapper.ChatMapper;
import com.ms.mal_back.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ChatRepository chatRepository;
    private final AdvertisementMapper advertisementMapper;
    private final ChatMapper chatMapper;

    @Override
    public List<UserResponse> searchUsers(String name, String phone, String role) {
        return userRepository.adminSearch(name, phone, role);
    }

    @Override
    public AdminUserDetailsResponse getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AdminUserDetailsResponse response = new AdminUserDetailsResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole().getName());

        var ads = advertisementRepository.findBySeller(user);
        var chats = chatRepository.findAllByUserIdWithAd(userId);

        List<AdvertisementResponse> adResponses = advertisementMapper.toSimpleDtoList(ads);
        List<ChatResponse> chatResponses = chatMapper.toChatResponseList(chats);

        response.setAdvertisements(adResponses);
        response.setChats(chatResponses);

        return response;
    }
}
