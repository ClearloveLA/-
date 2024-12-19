package com.campuscard.controller;

import com.campuscard.entity.Notice;
import com.campuscard.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;
    
    // 获取公告列表
    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getNotices() {
        return ResponseEntity.ok(noticeRepository.findAll());
    }
    
    // 创建公告
    @PostMapping("/notices")
    public ResponseEntity<Notice> createNotice(@RequestBody Notice notice) {
        notice.setId(UUID.randomUUID().toString());
        return ResponseEntity.ok(noticeRepository.save(notice));
    }
    
    // 更新公告
    @PatchMapping("/notices/{id}")
    public ResponseEntity<Notice> updateNotice(
            @PathVariable String id,
            @RequestBody Notice notice) {
        return noticeRepository.findById(id)
                .map(existing -> {
                    if (notice.getTitle() != null) {
                        existing.setTitle(notice.getTitle());
                    }
                    if (notice.getContent() != null) {
                        existing.setContent(notice.getContent());
                    }
                    if (notice.getTime() != null) {
                        existing.setTime(notice.getTime());
                    }
                    existing.setActive(notice.isActive());
                    return ResponseEntity.ok(noticeRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 删除公告
    @DeleteMapping("/notices/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable String id) {
        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 