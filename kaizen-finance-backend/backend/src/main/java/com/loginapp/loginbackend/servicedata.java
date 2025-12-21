package com.loginapp.loginbackend;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="servicedata")
public class servicedata {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String icon = "el-icon-service";  // 默认图标
    private Long count;
    private String lastUpdate;
    private String path;
    private String username;

    public servicedata(Long id, String title, String description, String icon, 
                      Long count, String lastUpdate, String path, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon != null ? icon : "el-icon-service";  // 如果为空则使用默认图标
        this.count = count;
        this.lastUpdate = lastUpdate;
        this.path = path;
        this.username = username;
    }

    // 获取默认图标
    public static String getDefaultIcon(String title) {
        switch(title.toLowerCase()) {
            case "pricing":
                return "el-icon-money";
            case "contract":
                return "el-icon-document";
            case "training":
                return "el-icon-school";
            default:
                return "el-icon-service";
        }
    }
}
