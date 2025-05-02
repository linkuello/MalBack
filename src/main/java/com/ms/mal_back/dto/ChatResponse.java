package com.ms.mal_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private Long id; // Chat ID
    private String adPhotoUrl; // First photo of the advertisement
    private String adAnimal;
    private String adBreed;
    private LocalDateTime lastMessageTime;
}


