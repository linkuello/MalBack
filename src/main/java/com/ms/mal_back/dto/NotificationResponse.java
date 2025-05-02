package com.ms.mal_back.dto;

import com.ms.mal_back.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;

    private NotificationType type;

    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;

    private Long targetId; // optional for deep linking to ad/chat/etc.
}
