package com.loginapp.loginbackend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.loginapp.loginbackend.repository.LoginRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class Login {
    @Autowired
    private LoginRepository loginRepository;

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody User user){
        try {
            System.out.println("Received login request for username: " + user.getUsername());
            
            Optional<User> userOptional = loginRepository.findByUsername(user.getUsername());
            System.out.println("User found: " + userOptional.isPresent());
            
            if(userOptional.isPresent()){
                User existingUser = userOptional.get();
                System.out.println("Received password: " + user.getPassword());
                System.out.println("Stored password: " + existingUser.getPassword());
                
                if(existingUser.getPassword().equals(user.getPassword())){
                    if(existingUser.getToken() == null){
                        existingUser.generateToken();
                    }   
                    User savedUser = loginRepository.save(existingUser);
                    
                    Map<String, String> response = new HashMap<>();
                    response.put("token", savedUser.getToken());
                    response.put("username", savedUser.getUsername());
                    
                    return ResponseEntity.ok(response);
                } else {
                    System.out.println("Password mismatch");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
                }
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } catch (Exception e) {
            e.printStackTrace();  // 添加这行来打印详细错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        System.out.println("Verifying token: " + token);
        
        Optional<User> userOptional = loginRepository.findByToken(token);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isTokenValid()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 200);
                response.put("username", user.getUsername());
                response.put("valid", true);
                
                System.out.println("Token verified successfully");
                return ResponseEntity.ok(response);
            }
        }
        
        System.out.println("Token verification failed");
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", 401);
        errorResponse.put("message", "Invalid token");
        errorResponse.put("valid", false);
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }
}






