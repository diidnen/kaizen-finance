package com.loginapp.sendemail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
@CrossOrigin(
  origins = {
    "http://3.8.135.139",
    "http://localhost:5173"
  },
  allowedHeaders = "*",
  allowCredentials = "true"
)
public class SendEmail {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of("message", "SendEmail controller is working!"));
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody FormData request) {
        try {
            // 打印接收数据
            System.out.println("Received request - Services: " + request.getServices());
            System.out.println("Email: " + request.getEmail());
            System.out.println("Name: " + request.getName());
            System.out.println("Phone: " + request.getPhone());
            System.out.println("BusinessType: " + request.getBusinessType());
            System.out.println("Turnover: " + request.getTurnover());
            System.out.println("VAT: " + request.isVat());
            System.out.println("Payroll: " + request.isPayroll());
            System.out.println("Bookkeeping: " + request.isBookkeeping());
            System.out.println("CountryOfTrade: " + request.getCountryOfTrade());
            System.out.println("FxCurrency: " + request.getFxCurrency());
            System.out.println("AdditionalInfo: " + request.getAdditionalInfo());

            // 验证必填字段
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email is required"));
            }
            if (request.getName() == null || request.getName().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Name is required"));
            }
            if (request.getPhone() == null || request.getPhone().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Phone is required"));
            }

            // 处理 businessType
            String businessType = request.getBusinessType();
            if ("other".equals(businessType) && request.getBusinessTypeOther() != null && !request.getBusinessTypeOther().isEmpty()) {
                businessType = request.getBusinessTypeOther();
            }
            
            // 存储数据库
            boolean success = storeinfo(
                    request.getEmail(), request.getName(), request.getPhone(),
                    businessType, request.getTurnover(),
                    request.isVat(), request.isPayroll(), request.isBookkeeping(),
                    request.getCountryOfTrade(), request.getFxCurrency(), request.getAdditionalInfo(),
                    request.getServices(), request.getOtherServicesText()
            );

            if (success) {
                // 尝试发送确认邮件，但不影响整体成功
                try {
                    sendConfirmationEmail(request.getEmail(), request.getName());
                    System.out.println("Confirmation email sent successfully");
                } catch (Exception e) {
                    System.err.println("Failed to send confirmation email: " + e.getMessage());
                    e.printStackTrace();
                    // 邮件发送失败不影响数据存储成功
                }
                return ResponseEntity.ok(Map.of("success", true, "message", "Data stored successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to store data"));
            }
        } catch (Exception e) {
            System.err.println("Error in sendEmail: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false, 
                    "message", "Internal server error: " + e.getMessage()
            ));
        }
    }

    private boolean storeinfo(String email, String name, String phone, String businessType,
                              String turnover, boolean vat, boolean payroll, boolean bookkeeping,
                              String countryOfTrade, String fxCurrency, String additionalInfo,
                              java.util.List<String> services, String otherServicesText) {
        Connection conn = null;
        try {
            System.out.println("Attempting to store contact info...");
            conn = getConnection();
            System.out.println("Database connection established");
            conn.setAutoCommit(false);
            
            // 首先检查并创建表
            createTableIfNotExists(conn);
            
            // 将 services 列表转换为 JSON 字符串
            String servicesJson = null;
            if (services != null && !services.isEmpty()) {
                servicesJson = String.join(",", services);
            }
            
            String sql = "INSERT INTO contact_info(email, name, phone, businessType, turnover, vat, payroll, bookkeeping, countryOfTrade, fxCurrency, additionalInfo, services, otherServicesText) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.setString(4, businessType);
            pstmt.setString(5, turnover);
            pstmt.setBoolean(6, vat);
            pstmt.setBoolean(7, payroll);
            pstmt.setBoolean(8, bookkeeping);
            pstmt.setString(9, countryOfTrade);
            pstmt.setString(10, fxCurrency);
            pstmt.setString(11, additionalInfo);
            pstmt.setString(12, servicesJson);
            pstmt.setString(13, otherServicesText);
            int result = pstmt.executeUpdate();
            conn.commit();
            System.out.println("Contact info stored successfully, rows affected: " + result);
            pstmt.close();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error storing contact info: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back");
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error storing contact info: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection closed");
                } catch (SQLException ex) {
                    System.err.println("Error closing connection: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }

    private void createTableIfNotExists(Connection conn) throws SQLException {
        // 首先创建表（如果不存在）
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS contact_info (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                email VARCHAR(255),
                name VARCHAR(255),
                phone VARCHAR(255),
                businessType VARCHAR(255),
                businessTypeOther VARCHAR(255),
                turnover VARCHAR(255),
                vat BOOLEAN,
                payroll BOOLEAN,
                bookkeeping BOOLEAN,
                countryOfTrade VARCHAR(255),
                fxCurrency VARCHAR(255),
                additionalInfo TEXT,
                services TEXT,
                otherServicesText TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("contact_info表创建成功或已存在");
            
            // 添加新列（如果不存在）
            try {
                stmt.execute("ALTER TABLE contact_info ADD COLUMN businessTypeOther VARCHAR(255)");
                System.out.println("Added businessTypeOther column");
            } catch (SQLException e) {
                System.out.println("Column businessTypeOther may already exist");
            }
            
            try {
                stmt.execute("ALTER TABLE contact_info ADD COLUMN services TEXT");
                System.out.println("Added services column");
            } catch (SQLException e) {
                System.out.println("Column services may already exist");
            }
            
            try {
                stmt.execute("ALTER TABLE contact_info ADD COLUMN otherServicesText TEXT");
                System.out.println("Added otherServicesText column");
            } catch (SQLException e) {
                System.out.println("Column otherServicesText may already exist");
            }
        }
    }

    private Connection getConnection() throws SQLException {
        // 添加调试信息
        System.out.println("=== 数据库连接调试信息 ===");
        System.out.println("数据库URL: " + dbUrl);
        System.out.println("用户名: " + dbUsername);
        System.out.println("密码: " + (dbPassword != null ? "***已设置***" : "***未设置***"));
        System.out.println("========================");
        
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired(required = false)
    private EmailService emailService;
    
    @Value("${email.service.provider:gmail}")
    private String emailProvider;

    private void sendConfirmationEmail(String toEmail, String customerName) {
        String subject = "Kaizen Solution - Request Received";
        String textContent = 
            "Dear " + customerName + ",\n\n" +
            "We have received your request and will contact you as soon as possible.\n" +
            "If your request is urgent, please contact us directly at info.kaizen4fs@gmail.com.\n\n" +
            "We will provide you with a login account and password soon. Please wait patiently.\n\n" +
            "Best regards,\nKaizen Solution Team";
        
        try {
            // 根据配置选择使用 API 服务或 Gmail SMTP
            if (!"gmail".equalsIgnoreCase(emailProvider) && emailService != null) {
                // 使用 API 服务 (SendGrid, Brevo, Mailgun)
                emailService.sendEmail(toEmail, subject, textContent);
                System.out.println("Email sent via " + emailProvider + " to: " + toEmail);
            } else {
                // 使用 Gmail SMTP
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("morrie3066924122@gmail.com"); // 你的发件邮箱
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(textContent);
                mailSender.send(message);
                System.out.println("Email sent via Gmail SMTP to: " + toEmail);
            }
        } catch (Exception e) {
            System.err.println("Error sending email to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            throw e; // 重新抛出异常以便上层处理
        }
    }


}