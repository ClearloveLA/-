package com.campuscard.controller;

import com.campuscard.entity.LostCard;
import com.campuscard.service.LostCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lost_cards")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
public class LostCardController {
    
    @Autowired
    private LostCardService lostCardService;
    
    @GetMapping
    public ResponseEntity<List<LostCard>> getAllLostCards() {
        return ResponseEntity.ok(lostCardService.findAll());
    }
    
    @PostMapping
    public ResponseEntity<LostCard> createLostCard(@RequestBody LostCard lostCard) {
        return ResponseEntity.ok(lostCardService.create(lostCard));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<LostCard> updateStatus(
            @PathVariable String id,
            @RequestBody LostCard lostCard) {
        return ResponseEntity.ok(lostCardService.updateStatus(id, lostCard.getStatus()));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLostCard(@PathVariable String id) {
        lostCardService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<LostCard> cancelLostCard(@PathVariable String id) {
        LostCard cancelledCard = lostCardService.cancelLostCard(id);
        return ResponseEntity.ok(cancelledCard);
    }
} 