package com.ms.mal_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSingularResponse {

    private Long id;

    private Long adId;
    private String adAnimal;
    private String adBreed;
    private BigDecimal adPrice;
    private String adPhotoUrl;

    private UserPreview otherUser;

    private List<MessageResponse> messages;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPreview {
        private Long id;
        private String username;
        private String photoUrl; // profile picture
    }
}
