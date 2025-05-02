package com.ms.mal_back.dto;

import com.ms.mal_back.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private NotificationType type;
    private String message;
    private Long targetId;
}
