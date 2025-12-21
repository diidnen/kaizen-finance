package com.loginapp.loginbackend;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String uploadedBy;
    private LocalDateTime uploadTime;
    private Boolean isActive = true;
    private String category; // 合同分类，如：服务合同、保密协议等
    private String username; // 合同针对的用户
} 