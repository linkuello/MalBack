package com.ms.mal_back.service;

import com.ms.mal_back.dto.NotificationRequest;
import com.ms.mal_back.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getNotificationsForUser(Long userId);
    boolean hasUnreadNotifications(Long userId);
    void markAllAsRead(Long userId);
    @Deprecated
    void sendSystemNotificationToAllUsers(NotificationRequest request);
}
