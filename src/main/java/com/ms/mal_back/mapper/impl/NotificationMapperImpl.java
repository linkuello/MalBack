package com.ms.mal_back.mapper.impl;

import com.ms.mal_back.dto.NotificationRequest;
import com.ms.mal_back.dto.NotificationResponse;
import com.ms.mal_back.entity.Notification;
import com.ms.mal_back.mapper.NotificationMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationResponse toDto(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt(),
                notification.getTargetId()
        );
    }

    @Override
    public List<NotificationResponse> toDtos(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public Notification toEntity(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setType(request.getType());
        notification.setMessage(request.getMessage());
        notification.setTargetId(request.getTargetId());
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }
}

