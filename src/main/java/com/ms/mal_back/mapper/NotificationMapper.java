package com.ms.mal_back.mapper;

import com.ms.mal_back.dto.NotificationRequest;
import com.ms.mal_back.dto.NotificationResponse;
import com.ms.mal_back.entity.Notification;

import java.util.List;

public interface NotificationMapper {
    NotificationResponse toDto(Notification notification);
    List<NotificationResponse> toDtos(List<Notification> notifications);
    @Deprecated
    Notification toEntity(NotificationRequest request); // optional/admin only
}
