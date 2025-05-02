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
public class MessageResponse {
    private String text;
    private LocalDateTime sentAt;
    private boolean isRead;
    private boolean isMine; // calculated in mapper using sender.id == currentUserId
}
