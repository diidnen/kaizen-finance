package com.loginapp.loginbackend;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="user")
@Data
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false,unique=true)
    private String email;

    private String token;

    @Column(name = "token_expiry")
    private java.time.LocalDateTime tokenExpiry;

    private String fullname;

    @Column(nullable=false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        createdAt = java.time.LocalDateTime.now();
        generateToken();
    }

    public void generateToken(){
        // 先清除旧token
        this.token = null;
        this.tokenExpiry = null;
        
        // 生成新token
        this.token = java.util.UUID.randomUUID().toString();
        this.tokenExpiry = java.time.LocalDateTime.now().plusDays(30);
    }
    
    public boolean isTokenValid() {
        return token!=null && tokenExpiry != null && 
               tokenExpiry.isAfter(java.time.LocalDateTime.now());
    }

    
   
}