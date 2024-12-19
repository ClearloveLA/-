package com.campuscard.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "lost_cards")
public class LostCard {
    @Id
    private String id;
    
    @Column(name = "card_id")
    private String cardId;
    
    private String name;
    private String phone;
    private String reason;
    private String status;
    private String time;
    
    @Column(name = "lost_time")
    private String lostTime;
    
    private String location;
    
    @Column(name = "user_id")
    private String userId;
} 