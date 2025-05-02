//package com.ms.mal_back.controller;
//
//import com.ms.mal_back.config.JwtService;
//import com.ms.mal_back.dto.NotificationResponse;
//import com.ms.mal_back.service.NotificationService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/notifications")
//@RequiredArgsConstructor
//public class NotificationController {
//
//    private final NotificationService notificationService;
//    private final JwtService jwtService;
//    @GetMapping
//    public ResponseEntity<List<NotificationResponse>> getNotifications(@RequestHeader("Authorization") String token) {
//        jwtService.validateToken(token);
//        Long userId = jwtService.extractUserId(token);
//
//        log.info("Fetching notifications for user {}", userId);
//        List<NotificationResponse> notifications = notificationService.getNotificationsForUser(userId);
//
//        return ResponseEntity.ok(notifications);
//    }
//    @GetMapping("/has-new")
//    public ResponseEntity<Boolean> hasNewNotifications(@RequestHeader("Authorization") String token) {
//        jwtService.validateToken(token);
//        Long userId = jwtService.extractUserId(token);
//
//        log.info("Checking for unread notifications for user {}", userId);
//        boolean hasNew = notificationService.hasUnreadNotifications(userId);
//
//        return ResponseEntity.ok(hasNew);
//    }
//}
