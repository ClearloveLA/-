package com.campuscard.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    
    @Column(unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String role;
    
    private String avatar;
} 