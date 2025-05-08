package com.ms.mal_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatSingularAdminResponse {
    private Long chatId;
    private Long adId;
    private String animal;
    private String breed;
    private BigDecimal price;
    private String adPhoto;

    private UserPreview seller;
    private UserPreview customer;

    private List<MessageResponse> messages;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserPreview {
        private Long id;
        private String username;
        private String photoUrl;
    }
}
