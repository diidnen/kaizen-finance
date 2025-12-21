package com.loginapp.loginbackend;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_documents")
public class UserDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String fileName;
    private String fileType;
    private String filePath;
    private LocalDateTime uploadTime;
    private String status = "pending"; // pending, approved, rejected
} 