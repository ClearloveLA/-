package com.campuscard.controller;

import com.campuscard.repository.LostCardRepository;
import com.campuscard.repository.FoundCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    
    @Autowired
    private LostCardRepository lostCardRepository;
    
    @Autowired
    private FoundCardRepository foundCardRepository;
    
    @GetMapping
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalLost", lostCardRepository.count());
        stats.put("totalFound", foundCardRepository.count());
        return ResponseEntity.ok(stats);
    }
} 