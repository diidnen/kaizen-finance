package com.loginapp.loginbackend;

import com.loginapp.loginbackend.entity.VerificationToken;
import com.loginapp.loginbackend.repository.VerificationTokenRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.time.LocalDateTime;
import java.util.UUID;
import java.net.URI;
import java.sql.*;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonProperty;


//@ComponentScan
//public class LoginApplication {
    // 这个注解告诉 Spring Boot：
    // "请扫描当前包及其子包中的所有组件"
    
    // 它会自动找到：
    // @Controller - REST 控制器
    // @Service - 服务层组件
    // @Repository - 数据访问层组件
    // @Component - 通用组件
//}
//@EnableAutoConfiguration
//public class Registrate {
    // 这个注解告诉 Spring Boot：
    // "请根据项目的依赖自动配置应用程序"
    
    // 例如：
    // - 如果有 spring-boot-starter-web，自动配置 web 服务器
    // - 如果有 spring-boot-starter-data-jpa，自动配置数据库连接
    // - 如果有 spring-boot-starter-security，自动配置安全功能
//}

/*@SpringBootConfiguration
public class LoginApplication {
    // 这个注解告诉 Spring Boot：
    // "这是一个配置类，你可以在这里定义 beans 和其他配置"
    
    @Bean  // 例如，你可以这样定义一个 bean
    public BCryptPasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
    //}
//}*/

@RestController
@RequestMapping("/api")   //自动配置：准备工具（创建 beans）
//自动装配：使用工具（注入 beans）
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Registrate {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // 检查用户是否存在
            if (isUserExists(request.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse("error", "Username already exists"));
            }

            // 生成验证token
            String token = UUID.randomUUID().toString();
            
            // 保存token和用户信息
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(token);
            verificationToken.setUsername(request.getUsername());
            verificationToken.setPassword(passwordEncoder.encode(request.getPassword()));
            verificationToken.setEmail(request.getEmail());
            verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
            tokenRepository.save(verificationToken);

            // 发送验证邮件
            sendVerificationEmail(request.getEmail(), token);

            return ResponseEntity.ok()
                .body(new ApiResponse("success", "Please check your email to complete registration"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationRequest request) {
        VerificationToken verificationToken = tokenRepository.findByToken(request.getToken());
        
        if (verificationToken == null || 
            verificationToken.isUsed() || 
            verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("error", "Invalid or expired token"));
        }

        try {
            // 注册用户
            registerUser(
                verificationToken.getUsername(),
                verificationToken.getPassword(),  // 已经加密的密码
                verificationToken.getEmail()
            );
            
            // 标记token已使用
            verificationToken.setUsed(true);
            tokenRepository.save(verificationToken);
            
            return ResponseEntity.ok()
                .body(new ApiResponse("success", "Registration completed successfully"));
        } catch (SQLException e) {
            System.err.println("Database error details:");
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(new ApiResponse("error", "Database error: " + e.getMessage()));
        }
    }

    private void sendVerificationEmail(String email, String verificationToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("morrie3066924122@gmail.com");
            message.setTo(email);
            message.setSubject("Registration Verification Code");
            message.setText("Your verification code is: " + verificationToken + 
                           "\nPlease enter this code to complete your registration.");
            
            System.out.println("Attempting to send email to: " + email);
            mailSender.send(message);
            System.out.println("Email sent successfully");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean registerUser(String username, String password, String email) throws SQLException {
        String sql = "INSERT INTO users (username, password, email, create_time) VALUES (?, ?, ?, NOW())";
        
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);  // 禁用自动提交
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, passwordEncoder.encode(password));
            pstmt.setString(3, email);
            
            int result = pstmt.executeUpdate();
            conn.commit();  // 显式提交事务
            
            return result > 0;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();  // 发生错误时回滚
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isUserExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/login_db?useSSL=false&serverTimezone=UTC",
                "root",
                "123456"
            );
            System.out.println("Database connection successful");
            return conn;
        } catch (SQLException e) {
            System.err.println("Database connection failed:");
            e.printStackTrace();
            throw e;
        }
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class VerificationRequest {
        private String token;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    // 内部类用于JSON响应
    private static class ApiResponse {
        private String status;
        private String message;

        public ApiResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        @JsonProperty
        public String getStatus() { return status; }
        @JsonProperty
        public String getMessage() { return message; }
    }
}