package com.campuscard.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "notices")
public class Notice {
    @Id
    private String id;
    
    private String title;
    private String content;
    private String time;
    private boolean isActive;
} 