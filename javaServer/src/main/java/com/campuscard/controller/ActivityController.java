package com.campuscard.controller;

import com.campuscard.entity.Activity;
import com.campuscard.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ActivityController {
    
    @Autowired
    private ActivityRepository activityRepository;
    
    @GetMapping("/activities")
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return ResponseEntity.ok(activities);
    }
} 