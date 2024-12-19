package com.campuscard.controller;

import com.campuscard.entity.Notification;
import com.campuscard.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;
    
    // 获取用户的通知列表
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam String userId) {
        return ResponseEntity.ok(
            notificationRepository.findByUserIdOrderByCreateTimeDesc(userId)
        );
    }
    
    // 创建通知
    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestBody Notification notification) {
        notification.setId(UUID.randomUUID().toString());
        notification.setCreateTime(LocalDateTime.now());
        notification.setRead(false);
        return ResponseEntity.ok(notificationRepository.save(notification));
    }
    
    // 标记通知为已读
    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable String id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if (notification.isPresent()) {
            Notification n = notification.get();
            n.setRead(true);
            return ResponseEntity.ok(notificationRepository.save(n));
        }
        return ResponseEntity.notFound().build();
    }
    
    // 标记所有通知为已读
    @PatchMapping("/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(@RequestParam String userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
        return ResponseEntity.ok().build();
    }
    
    // 获取未读通知数量
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(@RequestParam String userId) {
        return ResponseEntity.ok(
            notificationRepository.countByUserIdAndIsReadFalse(userId)
        );
    }
} 