package com.loginapp.loginbackend;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "testimonials")
public class Testimonial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String customerName;
    private String companyName;
    private String testimonial;
    private String avatarUrl;
    private String createdBy; // 创建此评价的管理员
    private LocalDateTime createdAt;
    private Boolean isActive = true;
    private Integer displayOrder = 0; // 显示顺序
}