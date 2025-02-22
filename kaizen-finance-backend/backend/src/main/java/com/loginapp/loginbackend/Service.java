package com.loginapp.loginbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.loginapp.loginbackend.repository.ServiceRepository;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.loginapp.loginbackend.servicedata;
import org.springframework.http.HttpStatus;
import com.loginapp.loginbackend.OrderData;
import com.loginapp.loginbackend.repository.OrderRepository;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import com.loginapp.loginbackend.repository.UserDocumentRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class Service {
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserDocumentRepository userDocumentRepository;
    
    @PostMapping("/list")
    public ResponseEntity<?> list(@RequestBody servicedata service) {
        System.out.println(service.getUsername());
        List<servicedata> services = serviceRepository.findByUsername(service.getUsername());
        System.out.println("Services found: " + services);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", services);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit-service")
    public ResponseEntity<?> submitService(@RequestBody Map<String, Object> request) {
        try {
            // 处理服务请求
            // ... 保存到数据库等操作 ...
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Service request submitted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();   
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to submit service request");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @PostMapping("/submit-order")
    public ResponseEntity<?> submitOrder(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            List<Map<String, Object>> services = (List<Map<String, Object>>) request.get("services");
            List<Map<String, Object>> enquiryServices = (List<Map<String, Object>>) request.get("enquiryServices");
            String orderDate = (String) request.get("orderDate");

            // 处理普通服务订单
            for (Map<String, Object> service : services) {
                OrderData order = new OrderData();
                order.setUsername(username);
                order.setServiceName((String) service.get("name"));
                // 获取价格并进行格式化
                Object priceObj = service.get("price");
                String formattedPrice = "£" + (priceObj != null ? priceObj.toString() : "0");
                order.setPrice(formattedPrice);
                order.setOrderDate(orderDate);
                // 设置服务时间（如果有）
                Object timeObj = service.get("time");
                if (timeObj != null) {
                    order.setServiceTime((String) timeObj);
                }
                order.setIsEnquiry(false);
                System.out.println("Saving order: " + order);
                orderRepository.save(order);
            }

            // 处理enquiry服务订单
            if (enquiryServices != null) {
                for (Map<String, Object> service : enquiryServices) {
                    OrderData order = new OrderData();
                    order.setUsername(username);
                    order.setServiceName((String) service.get("name"));
                    order.setPrice("Enquiry Required");
                    order.setOrderDate(orderDate);
                    // 设置服务时间（如果有）
                    Object timeObj = service.get("time");
                    if (timeObj != null) {
                        order.setServiceTime((String) timeObj);
                    }
                    order.setIsEnquiry(true);
                    System.out.println("Saving enquiry order: " + order);
                    orderRepository.save(order);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Order submitted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to submit order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @PostMapping("/ordersfind")
    public ResponseEntity<?> getOrders(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        System.out.println("getOrders method called for username: " + username);
        try {
            System.out.println("Fetching orders for username: " + username);
            
            List<OrderData> orders = orderRepository.findByUsername(username);
            System.out.println("Found " + orders.size() + " orders");
            
            // 计算总价格
            double totalPrice = 0.0;
            for (OrderData order : orders) {
                System.out.println("Processing order: " + order.getServiceName() + ", Price: " + order.getPrice());
                
                String price = order.getPrice();
                if (price != null && !price.equals("Enquiry Required")) {
                    // 移除£符号并转换为数字
                    String numericPrice = price.replace("£", "").trim();
                    try {
                        double orderPrice = Double.parseDouble(numericPrice);
                        totalPrice += orderPrice;
                        System.out.println("Added price: " + orderPrice + ", Total now: " + totalPrice);
                    } catch (NumberFormatException e) {
                        System.out.println("Failed to parse price: " + numericPrice);
                    }
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("orders", orders);
            response.put("totalPrice", String.format("£%.2f", totalPrice));
            
            System.out.println("Sending response with total price: " + String.format("£%.2f", totalPrice));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error fetching orders: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to fetch orders: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        System.out.println("Deleting order with ID: " + id);
        try {
            // 检查订单是否存在
            Optional<OrderData> orderOptional = orderRepository.findById(id);
            if (!orderOptional.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 404);
                response.put("message", "Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // 删除订单
            orderRepository.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Order deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to delete order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file,
                                          @RequestParam("username") String username) {
        try {
            // 创建上传目录
            String uploadDir = "uploads/" + username;
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一文件名
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            
            // 保存文件
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 保存文件信息到数据库
            UserDocument document = new UserDocument();
            document.setUsername(username);
            document.setFileName(file.getOriginalFilename());
            document.setFileType(file.getContentType());
            document.setFilePath(filePath.toString());
            document.setUploadTime(LocalDateTime.now());
            
            userDocumentRepository.save(document);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "Document uploaded successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to upload document: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/documents/{username}")
    public ResponseEntity<?> getUserDocuments(@PathVariable String username) {
        try {
            List<UserDocument> documents = userDocumentRepository.findByUsername(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("documents", documents);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to fetch documents: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
}

