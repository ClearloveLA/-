package com.campuscard.repository;

import com.campuscard.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    
    // 根据用户ID查询所有通知，按创建时间倒序排列
    List<Notification> findByUserIdOrderByCreateTimeDesc(String userId);
    
    // 查询用户未读通知
    List<Notification> findByUserIdAndIsReadFalse(String userId);
    
    // 统计用户未读通知数量
    Long countByUserIdAndIsReadFalse(String userId);
} 