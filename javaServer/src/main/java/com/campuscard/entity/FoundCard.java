package com.campuscard.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "found_cards")
public class FoundCard {
    @Id
    private String id;
    
    @Column(name = "card_id")
    private String cardId;
    
    private String location;
    private LocalDateTime time;
    private String contact;
    private String description;
    private String status;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Column(name = "user_id")
    private String userId;
    
    private String finder;
} 