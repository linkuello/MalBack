package com.ms.mal_back.dto.admin;

import com.ms.mal_back.dto.AdvertisementResponse;
import com.ms.mal_back.dto.ChatResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminUserDetailsResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private List<AdvertisementResponse> advertisements;
    private List<ChatResponse> chats;
}
