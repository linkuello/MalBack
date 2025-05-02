package com.ms.mal_back.service.impl;

import com.ms.mal_back.dto.NotificationRequest;
import com.ms.mal_back.dto.NotificationResponse;
import com.ms.mal_back.entity.Notification;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.mapper.NotificationMapper;
import com.ms.mal_back.repository.NotificationRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Override
    public List<NotificationResponse> getNotificationsForUser(Long userId) {
        List<Notification> notifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId);

        // Mark all as read
        markAllAsRead(userId);

        // Auto-delete notifications older than 30 days or more than 50 total
        List<Notification> toDelete = notifications.stream()
                .skip(50)
                .filter(n -> n.getCreatedAt().isBefore(LocalDateTime.now().minusDays(30)))
                .toList();

        if (!toDelete.isEmpty()) {
            notificationRepository.deleteAll(toDelete);
        }

        return notificationMapper.toDtos(notifications);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndIsReadFalse(userId);
        if (!unread.isEmpty()) {
            unread.forEach(n -> n.setRead(true));
            notificationRepository.saveAll(unread);
        }
    }

    @Override
    public boolean hasUnreadNotifications(Long userId) {
        return notificationRepository.existsByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Deprecated
    public void sendSystemNotificationToAllUsers(NotificationRequest request) {
        List<User> users = userRepository.findAll();

        List<Notification> notifications = users.stream()
                .map(user -> {
                    Notification n = new Notification();
                    n.setUser(user);
                    n.setType(request.getType());
                    n.setMessage(request.getMessage());
                    n.setTargetId(request.getTargetId());
                    n.setRead(false);
                    n.setCreatedAt(LocalDateTime.now());
                    return n;
                })
                .toList();
        notificationRepository.saveAll(notifications);
    }
}

