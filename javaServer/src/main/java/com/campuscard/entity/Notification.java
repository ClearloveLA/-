package com.campuscard.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    private String id;
    
    private String userId;
    private String type;
    private String title;
    private String content;
    private String relatedId;
    private boolean isRead;
    private LocalDateTime createTime;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }
} 