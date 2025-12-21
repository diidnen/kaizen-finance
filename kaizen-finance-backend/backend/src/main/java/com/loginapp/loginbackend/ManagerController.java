package com.loginapp.loginbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.loginapp.loginbackend.repository.LoginRepository;
import com.loginapp.loginbackend.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    
    @Autowired
    private LoginRepository loginRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    /**
     * 测试端点 - 验证API是否工作
     */
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Manager API is working!");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取所有用户信息（只有chuhan用户才能访问）
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> managerUser = loginRepository.findByToken(token);
            
            if (!managerUser.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User manager = managerUser.get();
            if (!"chuhan".equals(manager.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 获取所有用户
            List<User> allUsers = loginRepository.findAll();
            
            // 为每个用户添加token有效性状态
            for (User user : allUsers) {
                user.setToken(null); // 不返回token信息
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("users", allUsers);
            response.put("message", "Users retrieved successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to retrieve users: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 删除用户（只有chuhan用户才能操作，且不能删除自己）
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> managerUser = loginRepository.findByToken(token);
            
            if (!managerUser.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User manager = managerUser.get();
            if (!"chuhan".equals(manager.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 检查要删除的用户是否存在
            Optional<User> userToDelete = loginRepository.findById(id);
            if (!userToDelete.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 404);
                errorResponse.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            User user = userToDelete.get();
            
            // 不能删除manager账户
            if ("chuhan".equals(user.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Cannot delete manager account");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // 删除用户
            loginRepository.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "User deleted successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to delete user: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 重置用户密码（只有chuhan用户才能操作，且不能重置自己的密码）
     */
    @PostMapping("/users/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> managerUser = loginRepository.findByToken(token);
            
            if (!managerUser.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User manager = managerUser.get();
            if (!"chuhan".equals(manager.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 检查要重置密码的用户是否存在
            Optional<User> userToReset = loginRepository.findById(id);
            if (!userToReset.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 404);
                errorResponse.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            User user = userToReset.get();
            
            // 不能重置manager账户的密码
            if ("chuhan".equals(user.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Cannot reset manager password");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // 生成新密码（8位随机密码）
            String newPassword = generateRandomPassword();
            user.setPassword(newPassword);
            
            // 清除token，强制重新登录
            user.setToken(null);
            user.setTokenExpiry(null);
            
            // 保存用户
            loginRepository.save(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Password reset successfully");
            response.put("newPassword", newPassword);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to reset password: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 更新订单状态
     */
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> managerUser = loginRepository.findByToken(token);
            
            if (!managerUser.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User manager = managerUser.get();
            if (!"chuhan".equals(manager.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 检查订单是否存在
            Optional<OrderData> orderOptional = orderRepository.findById(id);
            if (!orderOptional.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 404);
                errorResponse.put("message", "Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            OrderData order = orderOptional.get();
            String newStatus = request.get("status");
            
            if (newStatus == null || newStatus.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Status cannot be empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // 验证状态值
            if (!isValidStatus(newStatus)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Invalid status. Valid statuses are: pending, approved, rejected, completed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // 更新状态
            order.setStatus(newStatus);
            OrderData savedOrder = orderRepository.save(order);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Order status updated successfully");
            response.put("order", savedOrder);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to update order status: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 验证状态值是否有效
     */
    private boolean isValidStatus(String status) {
        String[] validStatuses = {"pending", "approved", "rejected", "completed"};
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 创建新用户（管理员功能）
     */
    @PostMapping("/users")
    public ResponseEntity<?> createUser(
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> managerUser = loginRepository.findByToken(token);
            
            if (!managerUser.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User manager = managerUser.get();
            if (!"chuhan".equals(manager.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 获取请求参数
            String username = request.get("username");
            String password = request.get("password");
            String email = request.get("email");
            String fullname = request.get("fullname");
            
            // 验证必填字段
            if (username == null || username.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Username is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            if (password == null || password.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Password is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            if (email == null || email.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Email is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // 检查用户名是否已存在
            Optional<User> existingUser = loginRepository.findByUsername(username);
            if (existingUser.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 409);
                errorResponse.put("message", "Username already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }
            
            // 检查邮箱是否已存在
            Optional<User> existingEmail = loginRepository.findByEmail(email);
            if (existingEmail.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 409);
                errorResponse.put("message", "Email already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }
            
            // 创建新用户
            User newUser = new User();
            newUser.setUsername(username.trim());
            newUser.setPassword(password.trim());
            newUser.setEmail(email.trim());
            newUser.setFullname(fullname != null ? fullname.trim() : "");
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setToken(null);
            newUser.setTokenExpiry(null);
            
            User savedUser = loginRepository.save(newUser);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "User created successfully");
            response.put("user", savedUser);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to create user: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 生成随机密码
     */
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
    
    /**
     * 获取所有用户的订单信息
     */
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> managerUser = loginRepository.findByToken(token);
            
            if (!managerUser.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User manager = managerUser.get();
            if (!"chuhan".equals(manager.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 获取所有订单
            List<OrderData> allOrders = orderRepository.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("orders", allOrders);
            response.put("message", "Orders retrieved successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to retrieve orders: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * 修改订单价格
     */
    @PutMapping("/orders/{id}/price")
    public ResponseEntity<?> updateOrderPrice(
            @PathVariable Long id, 
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String authorization) {
        try {
            // 验证token和权限
            String token = authorization.replace("Bearer ", "");
            Optional<User> managerUser = loginRepository.findByToken(token);
            
            if (!managerUser.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User manager = managerUser.get();
            if (!"chuhan".equals(manager.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 403);
                errorResponse.put("message", "Access denied. Only managers can access this resource.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 检查订单是否存在
            Optional<OrderData> orderOptional = orderRepository.findById(id);
            if (!orderOptional.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 404);
                errorResponse.put("message", "Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            OrderData order = orderOptional.get();
            String newPrice = request.get("price");
            
            if (newPrice == null || newPrice.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 400);
                errorResponse.put("message", "Price cannot be empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // 更新价格
            order.setPrice(newPrice);
            OrderData savedOrder = orderRepository.save(order);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Price updated successfully");
            response.put("order", savedOrder);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to update price: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
} 