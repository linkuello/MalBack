package com.ms.mal_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileResponse {
    private Long id;
    private String username;
    private String phone;
    private String photoUrl;
    private String email;
    private List<AdvertisementResponse> advertisements;
}
