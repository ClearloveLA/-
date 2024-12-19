package com.campuscard.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String content;
    private String type;  // 活动类型：lost（挂失）或 found（招领）
    private LocalDateTime createTime;
    private String status;
} 