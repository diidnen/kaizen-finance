package com.loginapp.sendemail;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.sql.DriverManager;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class sendemail {
    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> request) {

        String email= request.get("email");
        String name= request.get("name");
        String phone= request.get("phone");
        String businessType= request.get("businessType");
        String turnover= request.get("turnover");
        boolean vat= Boolean.parseBoolean(request.get("vat"));
        boolean payroll= Boolean.parseBoolean(request.get("payroll"));
        boolean bookkeeping= Boolean.parseBoolean(request.get("bookkeeping"));
        String additionalInfo= request.get("additionalInfo");
        String countryOfTrade= request.get("countryOfTrade");
        String fxCurrency= request.get("fxCurrency");
        System.out.println(email);
        System.out.println(name);
        System.out.println(businessType);
        System.out.println(turnover);
        System.out.println(vat);
        System.out.println(payroll);
        System.out.println(bookkeeping);
        System.out.println(additionalInfo);

        boolean success = storeinfo(email, name, phone, businessType, turnover, 
            vat, payroll, bookkeeping, countryOfTrade, fxCurrency, additionalInfo);
        System.out.println(success);
        if (success) {
            return ResponseEntity.ok()
                .body(Map.of("success", true, "message", "Data stored successfully"));
        } else {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", "Failed to store data"));
        }
    }
    private boolean storeinfo(String email, String name, String phone, String businessType, String turnover, boolean vat, boolean payroll, boolean bookkeeping, String countryOfTrade, String fxCurrency, String additionalInfo) {
        String sql = "INSERT INTO contact_info(email, name, phone, businessType, turnover, vat, payroll, bookkeeping, countryOfTrade, fxCurrency, additionalInfo) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = getConnection();
            System.out.println("Database connected successfully");
            conn.setAutoCommit(false); 
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
            int result = pstmt.executeUpdate();
            System.out.println(result);
            System.out.println("Rows affected: " + result);
            conn.commit();
            System.out.println("Transaction committed");
            return result > 0;
        } catch(SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            if(conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back");
                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally{
            if(conn!=null){
                try{
                    conn.close();
                    
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }
    }
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/kaizen_finance",
            "root",
            "123456"
        );
    }
}
