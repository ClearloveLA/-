package com.campuscard.controller;

import com.campuscard.entity.FoundCard;
import com.campuscard.entity.LostCard;
import com.campuscard.entity.Notification;
import com.campuscard.repository.FoundCardRepository;
import com.campuscard.repository.LostCardRepository;
import com.campuscard.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FoundCardController {
    
    @Autowired
    private FoundCardRepository foundCardRepository;
    
    @Autowired
    private LostCardRepository lostCardRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    // 获取所有招领信息
    @GetMapping("/found_cards")
    public ResponseEntity<List<FoundCard>> getAllFoundCards() {
        return ResponseEntity.ok(foundCardRepository.findAll());
    }
    
    // 提交招领信息
    @PostMapping("/found_cards")
    public ResponseEntity<Map<String, Object>> createFoundCard(@RequestBody FoundCard foundCard) {
        // 设置基本信息
        foundCard.setId(UUID.randomUUID().toString());
        foundCard.setCreateTime(LocalDateTime.now());
        foundCard.setStatus("waiting");
        
        // 保存招领信息
        FoundCard saved = foundCardRepository.save(foundCard);
        
        // 查找匹配的挂失信息
        List<LostCard> matchedLostCards = lostCardRepository.findByCardIdAndStatus(
            foundCard.getCardId(), "pending");
        
        Map<String, Object> response = new HashMap<>();
        response.put("foundCard", saved);
        
        if (!matchedLostCards.isEmpty()) {
            LostCard matchedLost = matchedLostCards.get(0);
            
            // 更新挂失状态
            matchedLost.setStatus("completed");
            lostCardRepository.save(matchedLost);
            
            // 创建通知给失主
            Notification lostNotification = new Notification();
            lostNotification.setId(UUID.randomUUID().toString());
            lostNotification.setUserId(matchedLost.getUserId());
            lostNotification.setType("found_match");
            lostNotification.setTitle("您的校园卡已被找到");
            lostNotification.setContent(
                String.format("您挂失的校园卡(%s)已被拾获，请携带有效证件到%s认领。",
                    foundCard.getCardId(), foundCard.getLocation())
            );
            lostNotification.setRelatedId(foundCard.getCardId());
            lostNotification.setRead(false);
            lostNotification.setCreateTime(LocalDateTime.now());
            notificationRepository.save(lostNotification);
            
            // 创建通知给拾获者
            Notification foundNotification = new Notification();
            foundNotification.setId(UUID.randomUUID().toString());
            foundNotification.setUserId(foundCard.getUserId());
            foundNotification.setType("lost_match");
            foundNotification.setTitle("已找到失主");
            foundNotification.setContent(
                String.format("您拾获的校园卡(%s)已匹配到失主，请等待失主前来认领。",
                    foundCard.getCardId())
            );
            foundNotification.setRelatedId(foundCard.getCardId());
            foundNotification.setRead(false);
            foundNotification.setCreateTime(LocalDateTime.now());
            notificationRepository.save(foundNotification);
            
            response.put("matched", true);
            response.put("message", "已匹配到失主信息！");
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 获取单��招领信息
    @GetMapping("/found_cards/{id}")
    public ResponseEntity<FoundCard> getFoundCard(@PathVariable String id) {
        return foundCardRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 更新招领信息状态
    @PutMapping("/found_cards/{id}")
    public ResponseEntity<FoundCard> updateFoundCard(
            @PathVariable String id,
            @RequestBody FoundCard foundCard) {
        return foundCardRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(foundCard.getStatus());
                    return ResponseEntity.ok(foundCardRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 