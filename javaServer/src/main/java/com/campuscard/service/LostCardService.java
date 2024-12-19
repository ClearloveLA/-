package com.campuscard.service;

import com.campuscard.entity.LostCard;
import com.campuscard.entity.Notification;
import com.campuscard.repository.LostCardRepository;
import com.campuscard.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class LostCardService {
    @Autowired
    private LostCardRepository lostCardRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    public List<LostCard> findAll() {
        return lostCardRepository.findAll();
    }
    
    public LostCard create(LostCard lostCard) {
        lostCard.setId(UUID.randomUUID().toString());
        return lostCardRepository.save(lostCard);
    }
    
    public LostCard updateStatus(String id, String status) {
        LostCard lostCard = lostCardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("挂失记录不存在"));
        
        lostCard.setStatus(status);
        LostCard updatedCard = lostCardRepository.save(lostCard);
        
        // 创建状态更新通知
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setUserId(lostCard.getUserId());
        notification.setType("status_update");
        notification.setTitle("挂失状态更新");
        notification.setContent("您的校园卡挂失状态已更新为：" + getStatusText(status));
        notification.setRelatedId(lostCard.getCardId());
        notification.setRead(false);
        notification.setCreateTime(LocalDateTime.now());
        notificationRepository.save(notification);
        
        return updatedCard;
    }
    
    private String getStatusText(String status) {
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("pending", "处理中");
        statusMap.put("completed", "已完成");
        statusMap.put("cancelled", "已取消");
        return statusMap.getOrDefault(status, status);
    }

    public void delete(String id) {
        lostCardRepository.deleteById(id);
    }

    public LostCard cancelLostCard(String id) {
        LostCard lostCard = lostCardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("挂失记录不存在"));
        
        lostCard.setStatus("cancelled");
        return lostCardRepository.save(lostCard);
    }
} 